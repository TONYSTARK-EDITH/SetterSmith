package org.stark.settersmith.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateSetterAction extends AnAction {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateSetterAction.class);
    private static final String SETTER_GENERATED_MESSAGE = "Setter method calls added successfully.";

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        // Specify that this action requires running on the Event Dispatch Thread
        return ActionUpdateThread.EDT;
    }


    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) return;

        PsiElement elementAtCaret = getRelevantElementAtCaret(editor, psiFile);
        if (elementAtCaret == null) return;

        PsiVariable targetVariable = PsiTreeUtil.getParentOfType(elementAtCaret, PsiVariable.class);
        if (targetVariable == null) {
            LOG.warn("No variable found at caret. Action aborted.");
            return;
        }

        PsiClass variableClass = resolveClassFromType(targetVariable.getType());
        if (variableClass == null) {
            return;
        }

        PsiMethod enclosingMethod = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod.class);
        if (enclosingMethod == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> addSetterCallsToMethod(editor, enclosingMethod, targetVariable, variableClass, project));

        Messages.showInfoMessage(SETTER_GENERATED_MESSAGE, "Success");
    }

    private PsiElement getRelevantElementAtCaret(Editor editor, PsiFile psiFile) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAtCaret = psiFile.findElementAt(offset);

        if (elementAtCaret instanceof PsiJavaToken || elementAtCaret instanceof PsiWhiteSpace) {
            elementAtCaret = psiFile.findElementAt(offset - 1);
        }
        return elementAtCaret;
    }

    private void addSetterCallsToMethod(Editor editor, PsiMethod enclosingMethod, PsiVariable targetVariable, PsiClass variableClass, Project project) {
        PsiCodeBlock methodBody = enclosingMethod.getBody();
        if (methodBody == null) return;

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        Map<String, String> collectedParameters = new LinkedHashMap<>();

        // Collect all parameters from the methods
        List<String> setterCalls = Arrays.stream(variableClass.getMethods()).filter(method -> method.getName().startsWith("set"))
                .flatMap(method -> createSetterCallForMethod(method, targetVariable.getName(), collectedParameters).stream())
                .toList();


        try {
            Document document = getDocument(editor, setterCalls);
            PsiDocumentManager.getInstance(project).commitDocument(document);
        } catch (Exception ex) {
            LOG.error("Failed to insert call statement", ex);
        }

        addMissingParametersToMethodSignature(enclosingMethod, collectedParameters, elementFactory);
    }

    private static @NotNull Document getDocument(Editor editor, List<String> call) {
        Document document = editor.getDocument();
        int currentCaretOffset = editor.getCaretModel().getOffset();
        int currentLineNumber = document.getLineNumber(currentCaretOffset);
        int lineEndOffset = document.getLineEndOffset(currentLineNumber);
        String lineSeparator = "\n";
        // Extract the current line's text to determine its indentation.
        String currentLineText = document.getText(new TextRange(document.getLineStartOffset(currentLineNumber), lineEndOffset));
        // Use regex to capture leading whitespace.
        String currentIndentation = "";
        if (!currentLineText.isEmpty()) {
            currentIndentation = currentLineText.replaceAll("^(\\s*).*$", "$1");
        }

        StringBuilder insertedTextBuilder = new StringBuilder();
        insertedTextBuilder.append(lineSeparator);
        for (String callStmt : call) {
            insertedTextBuilder.append(currentIndentation)
                    .append(callStmt)
                    .append(lineSeparator);
        }
        String insertionText = insertedTextBuilder.toString();
        document.insertString(lineEndOffset, insertionText);
        return document;
    }

    private List<String> createSetterCallForMethod(PsiMethod method, String variableName, Map<String, String> parameterMap) {
        List<PsiParameter> parameters = List.of(method.getParameterList().getParameters());

        Map<String, String> localParameterMap = new LinkedHashMap<>();
        for (PsiParameter param : parameters) {
            String paramName = method.getName().substring(3);
            paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
            parameterMap.putIfAbsent(paramName, param.getType().getCanonicalText());
            localParameterMap.put(paramName, param.getType().getCanonicalText());
        }

        String callParameters = String.join(", ", localParameterMap.keySet());

        return List.of(variableName + "." + method.getName() + "(" + callParameters + ");");
    }

    private void addMissingParametersToMethodSignature(PsiMethod enclosingMethod, Map<String, String> newParameters, PsiElementFactory elementFactory) {
        PsiParameterList existingParameters = enclosingMethod.getParameterList();

        newParameters.forEach((name, type) -> {
            if (existingParameters.getParametersCount() == 0 ||
                    existingParameters.getParameters().length > 0 &&
                            PsiTreeUtil.findChildrenOfAnyType(existingParameters, PsiParameter.class).stream()
                                    .noneMatch(param -> param.getName().equals(name))) {
                try {
                    PsiParameter newParameter = elementFactory.createParameterFromText(type + " " + name, null);
                    existingParameters.add(newParameter);
                } catch (Exception ex) {
                    LOG.error("Failed to add parameter to method", ex);
                }
            }
        });
    }

    private PsiClass resolveClassFromType(PsiType type) {
        return type instanceof PsiClassType ? ((PsiClassType) type).resolve() : null;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(true);
    }
}
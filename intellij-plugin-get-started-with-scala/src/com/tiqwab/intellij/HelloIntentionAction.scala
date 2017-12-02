package com.tiqwab.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.{PsiFile, PsiLocalVariable}

/**
  * This is the sample of IntentionAction
  */
class HelloIntentionAction extends IntentionAction {

  // Intention name shown in the popup
  override def getText: String = "Hello"

  // ???
  override def getFamilyName: String = "Hello"

  // ???
  override def startInWriteAction(): Boolean = true

  // Define when this intention is available
  override def isAvailable(project: Project,
                           editor: Editor,
                           psiFile: PsiFile): Boolean = {
    // Get PsiElement at the current position
    // What is PsiElement: https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi_elements.html
    val caretModel = editor.getCaretModel
    val offset = caretModel.getOffset
    val psiElement = Option(psiFile.findElementAt(offset))

    // Check if the psiElement is local variable
    psiElement.exists(_.getParent.isInstanceOf[PsiLocalVariable])
  }

  // Define what to do when the intention is executed
  override def invoke(project: Project,
                      editor: Editor,
                      psiFile: PsiFile): Unit = println("Hello")

}

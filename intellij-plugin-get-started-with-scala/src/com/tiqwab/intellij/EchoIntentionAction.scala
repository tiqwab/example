package com.tiqwab.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class EchoIntentionAction extends IntentionAction {

  override def isAvailable(project: Project,
                           editor: Editor,
                           psiFile: PsiFile): Boolean = true

  override def startInWriteAction(): Boolean = true

  override def getText: String = "echo"

  override def invoke(project: Project,
                      editor: Editor,
                      psiFile: PsiFile): Unit = {
    println("hello")
  }

  override def getFamilyName: String = "echo"

}

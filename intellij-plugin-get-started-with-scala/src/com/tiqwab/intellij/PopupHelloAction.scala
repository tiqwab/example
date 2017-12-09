package com.tiqwab.intellij

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys,
}
import com.intellij.openapi.editor.Editor
import com.intellij.psi.{PsiFile, PsiLocalVariable}

/**
  * Sample of popup menu actions
  */
class PopupHelloAction extends AnAction {

  // Enable and disable action
  override def update(e: AnActionEvent): Unit = {
    val dataContext = e.getDataContext
    val isOnLocalVariableOpt = for {
      project <- Option(e.getProject)
      editor <- Option(e.getData[Editor](CommonDataKeys.EDITOR))
      psiFile <- Option(e.getData[PsiFile](CommonDataKeys.PSI_FILE))
      caretModel = editor.getCaretModel
      offset = caretModel.getOffset
      psiElement <- Option(psiFile.findElementAt(offset))
    } yield {
      psiElement.getParent.isInstanceOf[PsiLocalVariable]
    }
    isOnLocalVariableOpt match {
      case None =>
        e.getPresentation.setEnabledAndVisible(false)
      case Some(false) =>
        e.getPresentation.setEnabledAndVisible(false)
      case Some(true) =>
        e.getPresentation.setEnabledAndVisible(true)
    }
  }

  // Perform what to do with the action
  override def actionPerformed(anActionEvent: AnActionEvent): Unit =
    println("hello from popup")
}

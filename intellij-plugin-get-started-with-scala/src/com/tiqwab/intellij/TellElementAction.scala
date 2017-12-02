package com.tiqwab.intellij

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.refactoring.RefactoringFactory
import com.intellij.refactoring.actions.RenameElementAction

class TellElementAction extends AnAction {

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    val element = e.getData(CommonDataKeys.PSI_ELEMENT)
    println(element)
    /*
    val refactoring =
      RefactoringFactory.getInstance(project).createRename(element, "hoge")
    refactoring.run()
     */
    new RenameElementAction().actionPerformed(e)
  }

}

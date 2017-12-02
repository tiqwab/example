package com.tiqwab.intellij

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.refactoring.actions.RenameElementAction

/**
  * This is sample of calling other Action
  */
class DelegateSampleAction extends AnAction {

  override def actionPerformed(e: AnActionEvent): Unit = {
    val element = e.getData(CommonDataKeys.PSI_ELEMENT)
    println(element)
    new RenameElementAction().actionPerformed(e)
  }

}

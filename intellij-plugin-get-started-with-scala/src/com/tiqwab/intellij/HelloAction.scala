package com.tiqwab.intellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.ui.Messages

/**
  * This is sample of AnAction
  */
class HelloAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getProject
    val txt = Messages.showInputDialog(project,
                                       "What is your name?",
                                       "Input Your Name",
                                       Messages.getQuestionIcon)
    Messages.showMessageDialog(project,
                               s"Hello, $txt",
                               "Greeting",
                               Messages.getInformationIcon)
  }
}

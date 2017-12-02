package com.tiqwab.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.refactoring.actions.RenameElementAction

import scala.collection.JavaConverters._

class RenameIntentionAction extends IntentionAction {

  override def startInWriteAction(): Boolean = true

  override def getText: String = "Rename"

  override def getFamilyName: String = "Rename"

  override def isAvailable(project: Project,
                           editor: Editor,
                           psiFile: PsiFile): Boolean = {
    val caretModel = editor.getCaretModel
    val offset = caretModel.getOffset
    val psiElement = Option(psiFile.findElementAt(offset))
    psiElement exists { element =>
      // This condition should be same as '[Refactor] -> [Rename]' in the popup menu, so use RenameElementAction#update
      // Ref. https://githu  b.com/JetBrains/intellij-community/blob/master/platform/lang-impl/src/com/intellij/refactoring/actions/RenameElementAction.java
      val target = element.getParent
      val action = new RenameElementAction()
      val event = createEventFromAction(target, editor, action)
      action.update(event)
      val presentation = event.getPresentation
      presentation.isEnabledAndVisible
    }
  }

  override def invoke(project: Project,
                      editor: Editor,
                      psiFile: PsiFile): Unit = {
    // Ref. https://github.com/JetBrains/intellij-community/blob/master/spellchecker/src/com/intellij/spellchecker/quickfixes/RenameTo.java
    val caretModel = editor.getCaretModel
    val offset = caretModel.getOffset
    val psiElement = psiFile.findElementAt(offset)
    val action = new RenameElementAction()
    val event = createEventFromAction(psiElement, editor, action)
    action.actionPerformed(event)
  }

  private[this] def createEventFromAction(psiElement: PsiElement,
                                          editor: Editor,
                                          action: AnAction): AnActionEvent = {
    val map: java.util.Map[String, Object] = Map[String, AnyRef](
      CommonDataKeys.PSI_ELEMENT.getName -> psiElement).asJava

    val dataContext = SimpleDataContext.getSimpleContext(
      map,
      DataManager.getInstance().getDataContext(editor.getComponent))
    val action = new RenameElementAction()
    AnActionEvent.createFromAnAction(action, null, "", dataContext)
  }

}

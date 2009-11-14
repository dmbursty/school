#include "undo.hpp"

// Undo Stack

UndoStack::UndoStack() {

}

void UndoStack::add(Command* c) {
  // On an add we can throw away the whole redo stack
  while (!m_redo_stack.empty()) {
    Command* useless = m_redo_stack.top();
    m_redo_stack.pop();
    delete useless;
  }

  // Now add the newest command to the undo stack
  m_undo_stack.push(c);
}

void UndoStack::undo() {
  if (!m_undo_stack.empty()) {
    Command* c = m_undo_stack.top();
    m_undo_stack.pop();
    c->undo();
    m_redo_stack.push(c);
  }
}

void UndoStack::redo() {
  if (!m_redo_stack.empty()) {
    Command* c = m_redo_stack.top();
    m_redo_stack.pop();
    c->redo();
    m_undo_stack.push(c);
  }
}

void UndoStack::reset() {
  // Clear both stacks
  while (!m_redo_stack.empty()) {
    Command* useless = m_redo_stack.top();
    m_redo_stack.pop();
    delete useless;
  }
  while (!m_undo_stack.empty()) {
    Command* useless = m_undo_stack.top();
    m_undo_stack.pop();
    delete useless;
  }
}

// Command is one unit of undo has a set of subcommands
Command::Command() {
}

Command::~Command() {
  std::vector<MiniCommand*>::reverse_iterator it;
  for (it = subcommands.rbegin(); it < subcommands.rend(); it++) {
    delete *it;
  }
}

void Command::add(MiniCommand* mc) {
  subcommands.push_back(mc);
}

void Command::undo() {
  std::vector<MiniCommand*>::reverse_iterator it;
  for (it = subcommands.rbegin(); it < subcommands.rend(); it++) {
    (*it)->undo();
  }
}

void Command::redo() {
  std::vector<MiniCommand*>::iterator it;
  for (it = subcommands.begin(); it < subcommands.end(); it++) {
    (*it)->redo();
  }
}

// MiniCommand represents one rotation on one axis of one Joint
MiniCommand::MiniCommand(JointNode* node, double angle, bool x_axis)
  : node(node), angle(angle), x_axis(x_axis)
{
}

void MiniCommand::undo() {
  if (x_axis) {
    node->rotate_x(-angle);
  } else {
    node->rotate_y(-angle);
  }
}

void MiniCommand::redo() {
  if (x_axis) {
    node->rotate_x(angle);
  } else {
    node->rotate_y(angle);
  }
}

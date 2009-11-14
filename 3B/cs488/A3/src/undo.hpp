#ifndef UNDO_HPP
#define UNDO_HPP

#include <stack>
#include <vector>
#include "scene.hpp"

// Quick forward declaration
class UndoStack;
class Command;
class MiniCommand;


// The Undo Stack itself
class UndoStack {
public:
  UndoStack();

  void add(Command* c);
  void undo();
  void redo();
  void reset();

  int getUndoSize() {return m_undo_stack.size();}
  int getRedoSize() {return m_redo_stack.size();}

private:
  std::stack<Command*> m_undo_stack;
  std::stack<Command*> m_redo_stack;
};


// Command is one unit of undo has a set of subcommands
class Command {
public:
  Command();
  ~Command();

  void add(MiniCommand* mc);
  void undo();
  void redo();

  bool empty() {return subcommands.empty();}

private:
  std::vector<MiniCommand*> subcommands;
};


// MiniCommand represents one rotation on one axis of one Joint
class MiniCommand {
public:
  MiniCommand(JointNode* node, double angle, bool x_axis);

  void undo();
  void redo();

private:
  JointNode* node;
  double angle;
  bool x_axis;
};

#endif

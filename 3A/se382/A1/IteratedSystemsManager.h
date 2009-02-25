// Author: Michael Terry

#ifndef ITERATEDSYSTEMSMANAGER_H_
#define ITERATEDSYSTEMSMANAGER_H_

#include "lsystem/IteratedSystem.h"

namespace cs349
{
/*
 * This class manages a list of all available iterated systems.
 * In this implementation, we automatically add the five included
 * systems, but you are free to add more.
 *
 * This object is a singleton -- call IteratedSystemsManager::GetInstance
 * to obtain a pointer to the manager.
 */
class IteratedSystemsManager
{
 private:
  static IteratedSystemsManager* s_Instance;

  vector<IteratedSystem*> systems;
  IteratedSystemsManager();

 public:
  static IteratedSystemsManager* GetInstance();
  virtual ~IteratedSystemsManager();

  void AddSystem(IteratedSystem* system);
  int GetNumSystems() const;
  IteratedSystem* GetSystem(int systemNum) const;
};

}

#endif /*ITERATEDSYSTEMSMANAGER_H_*/

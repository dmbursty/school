// Author: Michael Terry

#include "IteratedSystemsManager.h"

#include "lsystem/FractalFern.h"
#include "lsystem/DragonCurve.h"
#include "lsystem/FractalPlant.h"
#include "lsystem/KochCurve.h"
#include "lsystem/SierpinskiTriangle.h"

namespace cs349
{

IteratedSystemsManager* IteratedSystemsManager::s_Instance = NULL;

IteratedSystemsManager::IteratedSystemsManager()
{
	s_Instance = this;
	this->AddSystem(new FractalFern());
	this->AddSystem(new FractalPlant());
	this->AddSystem(new KochCurve());
	this->AddSystem(new SierpinskiTriangle());
	this->AddSystem(new DragonCurve());
}

IteratedSystemsManager::~IteratedSystemsManager()
{
	for (vector<IteratedSystem*>::iterator iter=this->systems.begin(); iter != this->systems.end(); iter++) {
		delete(*iter);
	}
	this->systems.clear();

}

IteratedSystemsManager* IteratedSystemsManager::GetInstance()
{
	if (s_Instance == NULL) {
		s_Instance = new IteratedSystemsManager();
	}
	return s_Instance;
}

void IteratedSystemsManager::AddSystem(IteratedSystem* system)
{
	this->systems.push_back(system);
}

int IteratedSystemsManager::GetNumSystems() const
{
	return this->systems.size();
}

IteratedSystem* IteratedSystemsManager::GetSystem(int systemNum) const
{
	return this->systems[systemNum];
}

}

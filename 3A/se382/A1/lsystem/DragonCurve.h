#ifndef DRAGONCURVE_H_
#define DRAGONCURVE_H_

#include <string>
using namespace std;

#include "IteratedSystem.h"
#include "TurtleGraphics.h"

namespace cs349
{

class DragonCurve : public cs349::IteratedSystem
{
private:
	TurtleGraphics turtle;
	vector<Point>  points;
	string         curString;

protected:	
	virtual void IterateSystem();

public:
	DragonCurve();
	virtual ~DragonCurve();

	virtual string 			GetName() const;
	virtual void 			SetParamValue(int paramNum, int value);
	virtual void			Reset();

	virtual vector<Point> 	GetLines(const Rectangle & displayArea) const;
};

}

#endif /*DRAGONCURVE_H_*/

#ifndef KOCHCURVE_H_
#define KOCHCURVE_H_

#include <string>
using namespace std;

#include "IteratedSystem.h"
#include "TurtleGraphics.h"

namespace cs349
{

class KochCurve : public cs349::IteratedSystem
{
private:
	TurtleGraphics turtle;
	vector<Point>  points;
	string         curString;
	
protected:	
	virtual void IterateSystem();

public:
	KochCurve();
	virtual ~KochCurve();

	virtual string 			GetName() const;

	virtual void 			SetParamValue(int paramNum, int value);

	virtual void 			Reset();

	virtual vector<Point> 	GetLines(const Rectangle & displayArea) const;
};

}

#endif /*KOCHCURVE_H_*/

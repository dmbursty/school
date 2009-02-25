#ifndef SIERPINSKITRIANGLE_H_
#define SIERPINSKITRIANGLE_H_

#include <string>
using namespace std;

#include "IteratedSystem.h"
#include "TurtleGraphics.h"

namespace cs349
{

class SierpinskiTriangle : public cs349::IteratedSystem
{
private:
	TurtleGraphics turtle;
	vector<Point>  points;
	string         curString;
	int			   curAngle;
	
protected:	
	virtual void IterateSystem();

public:
	SierpinskiTriangle();
	virtual ~SierpinskiTriangle();

	virtual string 			GetName() const;

	virtual void 			SetParamValue(int paramNum, int value);

	virtual void 			Reset();

	virtual vector<Point> 	GetLines(const Rectangle & displayArea) const;
};

}

#endif /*SIERPINSKITRIANGLE_H_*/

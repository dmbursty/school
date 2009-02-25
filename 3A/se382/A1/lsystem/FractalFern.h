#ifndef FRACTALFERN_H_
#define FRACTALFERN_H_

#include "IteratedSystem.h"

namespace cs349
{

class FractalFern : public cs349::IteratedSystem
{
private:
	vector<Point>	points;
	Point 			lastPoint;
	
protected:	
	virtual void IterateSystem();

public:
	FractalFern();
	virtual ~FractalFern();

	virtual string 			GetName() const;
	virtual void 			SetParamValue(int paramNum, int value);
	virtual void Reset();

	virtual vector<Point>	GetPoints(const Rectangle & displayArea) const;
};

}

#endif /*FRACTALFERN_H_*/

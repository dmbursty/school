#ifndef FRACTALPLANT_H_
#define FRACTALPLANT_H_

#include <string>
using namespace std;

#include "IteratedSystem.h"
#include "TurtleGraphics.h"

namespace cs349
{

class FractalPlant : public cs349::IteratedSystem
{
private:
	TurtleGraphics 			turtle;
	vector<vector<Point> >	lines;
	string         			curString;
	int						angleParam;
	
protected:	
	virtual void IterateSystem();

public:
	FractalPlant();
	virtual ~FractalPlant();

	virtual string 			GetName() const;

	virtual int 			GetNumParams() const;
	virtual int 			GetParamValue() const;
	virtual pair<int, int>	GetParamMinMax(int paramNum) const;
	virtual string 			GetParamName(int paramNum) const;
	virtual void 			SetParamValue(int paramNum, int value);

	virtual void Reset();

	virtual vector<vector<Point> > GetLineSegments(const Rectangle & displayArea) const;
};

}

#endif /*FRACTALPLANT_H_*/

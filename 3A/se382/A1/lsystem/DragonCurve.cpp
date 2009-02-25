#include "DragonCurve.h"

#define MAX_ITERATIONS 15

namespace cs349
{

DragonCurve::DragonCurve() : IteratedSystem(MAX_ITERATIONS)
{
	this->Reset();
}

DragonCurve::~DragonCurve()
{
}

string DragonCurve::GetName() const
{
	return string("Dragon Curve");
}

void DragonCurve::SetParamValue(int paramNum, int value)
{
	; // no-op
}

void DragonCurve::Reset()
{
	IteratedSystem::Reset();
	curString = string("FX");
	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointRight();
	this->points.clear();
	this->points.push_back(this->turtle.GetLocation());
}

void DragonCurve::IterateSystem()
{
	// Algorithm source: http://en.wikipedia.org/wiki/L-system
	string newString;

	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointRight();
	this->points.clear();
	this->points.push_back(this->turtle.GetLocation());

	for (string::iterator iter=this->curString.begin(); iter != this->curString.end(); iter++) {
		char c = (*iter);
		if (c == 'F') {
			this->points.push_back(this->turtle.MoveForward());
			newString.push_back(c);
		} else if (c == 'X') {
			newString.append("X+YF+");
		} else if (c == 'Y') {
			newString.append("-FX-Y");
		} else if (c == '-') {
			this->turtle.Turn(-90);
			newString.push_back(c);
		} else if (c == '+') {
			this->turtle.Turn(90);
			newString.push_back(c);
		}
	}
	this->curString = newString;
}

vector<Point> DragonCurve::GetLines(const Rectangle & displayArea) const
{
	return this->ScalePointsToArea(this->points, displayArea);
}

}

#include "KochCurve.h"

#define MAX_ITERATIONS 5

namespace cs349
{

KochCurve::KochCurve() : IteratedSystem(MAX_ITERATIONS)
{
	this->Reset();
}

KochCurve::~KochCurve()
{
}

string KochCurve::GetName() const
{
	return string("Koch Curve");
}

void KochCurve::SetParamValue(int paramNum, int value)
{
	; // no-op
}

void KochCurve::Reset()
{
	IteratedSystem::Reset();
	curString = string("F");
	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointRight();
	this->points.clear();
	this->points.push_back(this->turtle.GetLocation());
}

void KochCurve::IterateSystem()
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
			newString.append("F+F-F-F+F");
		} else if (c == '-') {
			this->turtle.Turn(90);
			newString.push_back(c);
		} else if (c == '+') {
			this->turtle.Turn(-90);
			newString.push_back(c);
		}
	}
	this->curString = newString;
}

vector<Point> KochCurve::GetLines(const Rectangle & displayArea) const
{
	return this->ScalePointsToArea(this->points, displayArea);
}

}

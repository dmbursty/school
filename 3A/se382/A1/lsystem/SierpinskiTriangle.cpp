#include "SierpinskiTriangle.h"

#define MAX_ITERATIONS 9

namespace cs349
{

SierpinskiTriangle::SierpinskiTriangle() : IteratedSystem(MAX_ITERATIONS)
{
	this->Reset();
}

SierpinskiTriangle::~SierpinskiTriangle()
{
}

string SierpinskiTriangle::GetName() const
{
	return string("SierpinskiTriangle");
}

void SierpinskiTriangle::SetParamValue(int paramNum, int value)
{
	; // no-op
}

void SierpinskiTriangle::Reset()
{
	IteratedSystem::Reset();
	curString = string("A");
	turtle.SetLocation(Point(0,0));
	turtle.PointRight();
	this->points.clear();
	this->points.push_back(this->turtle.GetLocation());
	this->curAngle = 60;
}

void SierpinskiTriangle::IterateSystem()
{
	// Algorithm source: http://en.wikipedia.org/wiki/L-system

	string newString;
	
	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointRight();
	this->points.clear();
	this->points.push_back(this->turtle.GetLocation());
	
	for (string::iterator iter=this->curString.begin(); iter != this->curString.end(); iter++) {
		char c = (*iter);
		if (c == 'A') {
			this->points.push_back(this->turtle.MoveForward());
			newString.append("B-A-B");
		} else if (c == 'B') {
			this->points.push_back(this->turtle.MoveForward());
			newString.append("A+B+A");
		} else if (c == '-') {
			this->turtle.Turn(this->curAngle);
			newString.push_back(c);
		} else if (c == '+') {
			this->turtle.Turn(-1 * this->curAngle);
			newString.push_back(c);
		}
	}
	this->curString = newString;
	this->curAngle = this->curAngle * -1;
}

vector<Point> SierpinskiTriangle::GetLines(const Rectangle & displayArea) const
{
	return this->ScalePointsToArea(this->points, displayArea);
}

}

#include "FractalPlant.h"

#define MAX_ITERATIONS 7

namespace cs349
{

FractalPlant::FractalPlant() : IteratedSystem(MAX_ITERATIONS)
{
	this->angleParam = 25;
	this->Reset();
}

FractalPlant::~FractalPlant()
{
}

string FractalPlant::GetName() const
{
	return string("Fractal Plant");
}

int FractalPlant::GetNumParams() const
{
	return 1;
}

int FractalPlant::GetParamValue() const
{
	return this->angleParam;
}

pair<int, int>	FractalPlant::GetParamMinMax(int paramNum) const
{
	return pair<int,int>(5,75);
}

string FractalPlant::GetParamName(int paramNum) const
{
	return string("Angle");
}

void FractalPlant::SetParamValue(int paramNum, int value)
{
	pair<int,int> minMax = this->GetParamMinMax(paramNum);
	value = max(value, minMax.first);
	value = min(value, minMax.second);
	this->angleParam = value;
}

void FractalPlant::Reset()
{
	IteratedSystem::Reset();
	curString = string("X");
	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointUp();
	this->turtle.Turn(20);
	this->lines.clear();
}

void FractalPlant::IterateSystem()
{
	// Algorithm source: http://en.wikipedia.org/wiki/L-system

	string newString;

	this->turtle.SetLocation(Point(0,0));
	this->turtle.PointUp();
	this->turtle.Turn(20);
	this->lines.clear();
	
	vector<Point> positionStack;
	vector<Point> angleStack;

	vector<Point> thisSet;
	thisSet.push_back(Point(0,0));
	for (string::iterator iter=this->curString.begin(); iter != this->curString.end(); iter++) {
		char c = (*iter);
		if (c == 'F') {
			thisSet.push_back(this->turtle.MoveForward());
			newString.append("FF");
		} else if (c == 'X') {
			newString.append("F-[[X]+X]+F[+FX]-X");
		} else if (c == '[') {
			positionStack.insert(positionStack.begin(), this->turtle.GetLocation());
			angleStack.insert(angleStack.begin(), this->turtle.GetAngleVector());
			newString.push_back(c);
		} else if (c == ']') {
			this->turtle.SetLocation(positionStack.front());
			this->turtle.SetAngleVector(angleStack.front());
			positionStack.erase(positionStack.begin());
			angleStack.erase(angleStack.begin());
			newString.push_back(c);
			this->lines.push_back(thisSet);
			thisSet.clear();
			thisSet.push_back(this->turtle.GetLocation());
		} else if (c == '-') {
			this->turtle.Turn(-1*this->angleParam);
			newString.push_back(c);
		} else if (c == '+') {
			this->turtle.Turn(this->angleParam);
			newString.push_back(c);
		}
	}
	this->curString = newString;
}

vector<vector<Point> > FractalPlant::GetLineSegments(const Rectangle & displayArea) const
{
	return this->ScaleLineSegmentsToArea(this->lines, displayArea);
}

}

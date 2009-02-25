#include "FractalFern.h"
#include <stdlib.h>
#include <time.h>

#define MAX_ITERATIONS 100

namespace cs349
{

FractalFern::FractalFern() : IteratedSystem(MAX_ITERATIONS)
{
	srand(time(NULL));
}

FractalFern::~FractalFern()
{
}

string FractalFern::GetName() const
{
	return string("Fractal Fern");
}

void FractalFern::SetParamValue(int paramNum, int value)
{
	; // no-op
}

void FractalFern::Reset()
{
	IteratedSystem::Reset();
	this->points.clear();
	this->lastPoint = Point();
}

void FractalFern::IterateSystem()
{
	// Code derived from http://en.wikipedia.org/wiki/Iterated_function_system
	const int NUM_ITERATIONS = 100;
	for (int i = 0; i < NUM_ITERATIONS; i++) {
		int value = rand() % 100;
		Point p;
		if (value <= 1) {
			p.x = 0;
			p.y = 0.16 * this->lastPoint.y;
		} else if (value <= 8) {
			p.x = 0.2 * this->lastPoint.x - 0.26 * this->lastPoint.y;
			p.y = 0.23 * this->lastPoint.x  + 0.22 * this->lastPoint.y + 1.6;
		} else if (value <= 15) {
			p.x = -0.15 * this->lastPoint.x + 0.28 * this->lastPoint.y;
			p.y = 0.26 * this->lastPoint.x + 0.24 * this->lastPoint.y + 0.44;
		} else {
			p.x = 0.85 * this->lastPoint.x + 0.04 * this->lastPoint.y;
			p.y = -0.04 * this->lastPoint.x + 0.85 * this->lastPoint.y + 1.6;
		}
		this->points.push_back(p);
		this->lastPoint = p;
	}
}

vector<Point> FractalFern::GetPoints(const Rectangle & displayArea) const
{
	// From article ref'ed above, all points will fall within this region
	Rectangle srcArea(-2.2, 0, 2.7+2.2, 10);
	return this->ScalePointsToArea(this->points, srcArea, displayArea);
}

}

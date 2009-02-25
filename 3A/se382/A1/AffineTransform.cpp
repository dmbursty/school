#include "AffineTransform.h"
#include <math.h>
#include <iostream>
using namespace std;
namespace cs349
{

AffineTransform::AffineTransform()
{
	this->SetToIdentity();
}
AffineTransform::AffineTransform(const SquareMatrix & m)
{
	this->transform = m;
}

AffineTransform::~AffineTransform()
{
}

void AffineTransform::AppendMatrix(const AffineTransform & m)
{
	this->transform = this->transform * m.GetMatrix();
}

AffineTransform AffineTransform::GetIdentityMatrix()
{
	return AffineTransform();
}

AffineTransform AffineTransform::GetInverse() const
{
	SquareMatrix t = this->transform;
	t.makeInverse();
	
	return AffineTransform(t);
}

SquareMatrix AffineTransform::GetMatrix() const
{
	return this->transform;
}

AffineTransform AffineTransform::GetRotationMatrix(double theta)
{
	AffineTransform t;
	t.SetToRotation(theta);
	return t;
}

AffineTransform AffineTransform::GetScaleMatrix(double scaleX, double scaleY)
{
	AffineTransform t;
	t.SetToScale(scaleX, scaleY);
	return t;
}
AffineTransform AffineTransform::GetTranslationMatrix(double translateX, double translateY)
{
	AffineTransform t;
	t.SetToTranslation(translateX, translateY);
	return t;
}
	
void AffineTransform::PrependMatrix(const AffineTransform & m)
{
	this->transform = m.GetMatrix() * this->transform;
}

void AffineTransform::Rotate(double theta)
{
	this->PrependMatrix(AffineTransform::GetRotationMatrix(theta));
}

void AffineTransform::RotateInDegrees(double angle)
{
	this->Rotate(angle * PI / 180.0);
}

void AffineTransform::Scale(double scaleX, double scaleY)
{
	this->PrependMatrix(AffineTransform::GetScaleMatrix(scaleX, scaleY));
}
void AffineTransform::Translate(double translateX, double translateY)
{
	this->PrependMatrix(AffineTransform::GetTranslationMatrix(translateX, translateY));
}
	
void AffineTransform::SetToIdentity()
{
	this->transform.makeUnity();
}

void AffineTransform::SetToRotation(double theta)
{
	double cosTheta = cos(theta);
	double sinTheta = sin(theta);
	
	this->SetToIdentity();
	this->transform[0][0] = cosTheta;
	this->transform[1][0] = -1 * sinTheta;
	this->transform[0][1] = sinTheta;
	this->transform[1][1] = cosTheta;
}
void AffineTransform::SetToScale(double scaleX, double scaleY)
{
	this->SetToIdentity();
	this->transform[0][0] = scaleX;
	this->transform[1][1] = scaleY;
}
void AffineTransform::SetToTranslation(double translateX, double translateY)
{
	this->SetToIdentity();
	this->transform[2][0] = translateX;
	this->transform[2][1] = translateY;
}
	
Point AffineTransform::Transform(const Point & p) const
{
	PointMatrix pointMatrix;
	pointMatrix[0][0] = p.x;
	pointMatrix[0][1] = p.y;
	pointMatrix[0][2] = 1;
	pointMatrix = pointMatrix * this->transform;
	
	return Point(pointMatrix[0][0], pointMatrix[0][1]);
}
	

}

// Author: Michael Terry

#ifndef AFFINETRANSFORM_H_
#define AFFINETRANSFORM_H_

#include "matrix/stat_smatrix.h"
typedef CwMtx::CWTSSquareMatrix<3> SquareMatrix;
typedef CwMtx::CWTSMatrix<1,3> PointMatrix;

#include "Point.h"
#include "Rectangle.h"

namespace cs349
{

/*
 * Supports affine transformations of points.
 */
class AffineTransform
{
 private:
  SquareMatrix transform;

 public:
  static const double PI = 3.14159265358979323846;

  AffineTransform();
  AffineTransform(const SquareMatrix & m);
  virtual ~AffineTransform();

  static AffineTransform GetIdentityMatrix();
  static AffineTransform GetRotationMatrix(double theta);
  static AffineTransform GetScaleMatrix(double scaleX, double scaleY);
  static AffineTransform GetTranslationMatrix(double translateX, double translateY);

  virtual void Rotate(double theta);
  virtual void RotateInDegrees(double angle);
  virtual void Scale(double scaleX, double scaleY);
  virtual void Translate(double translateX, double translateY);

  virtual void SetToIdentity();
  virtual void SetToRotation(double theta);
  virtual void SetToScale(double scaleX, double scaleY);
  virtual void SetToTranslation(double translateX, double translateY);

  Point Transform(const Point & p) const;

  virtual void PrependMatrix(const AffineTransform & m);
  virtual void AppendMatrix(const AffineTransform & m);

  virtual AffineTransform GetInverse() const;

  virtual SquareMatrix GetMatrix() const;
};

}

#endif /*AFFINETRANSFORM_H_*/

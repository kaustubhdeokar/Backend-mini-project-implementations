package main

import "fmt"

func InterfacesHelper() {
	r := Rectangle{width: 10, height: 2}
	c := Circle{radius: 5}
	fmt.Printf("Rectangle area: %.2f\n", r.Area())
	fmt.Printf("Circle area: %.2f\n", c.Area())

	shapes := []Shape{r, c}
	for _, shape := range shapes {
		fmt.Println(shape.Area())
	}

	measureables := []Measureable{r, c}
	for _, measureable := range measureables {
		fmt.Println(measureable.Perimeter())
	}

	geometries := []Geometry{r, c}
	for _, geometry := range geometries {
		someFunc(geometry)
		fmt.Println(geometry.describeGeometry())
	}

}

type Geometry interface {
	Shape
	Measureable
	describeGeometry() string
}

type Shape interface {
	Area() float64
}
type Measureable interface {
	Perimeter() int64
}

type Rectangle struct {
	width, height float64
}

func (r Rectangle) Area() float64 {
	return r.width * r.width
}
func (r Rectangle) Perimeter() int64 {
	return int64(2 * (r.width + r.height))
}
func (r Rectangle) describeGeometry() string {
	return "This is a rectange"
}

type Circle struct {
	radius float64
}

func (c Circle) Area() float64 {
	return 3.14 * c.radius * c.radius
}
func (c Circle) Perimeter() int64 {
	return int64(2 * 3.14 * c.radius)
}
func (c Circle) describeGeometry() string {
	return "This is a circle"
}

func someFunc(g Geometry) {
	fmt.Println(g.Area())
	fmt.Println(g.Perimeter())
}

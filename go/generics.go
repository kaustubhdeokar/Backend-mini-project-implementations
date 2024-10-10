package main

type Number interface {
	int | int32 | int64 | float32 | float64
}

func addNums[T Number](numbers []T) T {
	var result T
	for _, num := range numbers {
		result += num
	}
	return result
}

func findHighest[T Number] (numbers []T) T{
	var result = numbers[0]
	for _, num := range numbers {
		if num > result {
			result = num
		}
	}
	return result
}

func genericsHelpers() {
	nums := []int{1, 2, 3, 4, 5}
	println(addNums(nums))
	println(findHighest(nums))

	nums1 := []int32{1, 2, 3, 4, 5}
	println(addNums(nums1))
println(findHighest(nums1))
	//10^18
	nums2 := []int64{1000000000000000000, 2000000000000000000, 3000000000000000000, 3000000000000000000}
	println(addNums(nums2))
	println(findHighest(nums2))
	nums3 := []float32{1.1, 2.2, 3.3, 4.4, 5.5}
	println(addNums(nums3))
	println(findHighest(nums3))
	nums4 := []float64{1000000000000000000.1, 2000000000000000000.2, 3000000000000000000.3, 4000000000000000000.4, 5.5}
	println(addNums(nums4))
	println(findHighest(nums4))
}

// Filename Plant.as 
class Complex { 
	var re:Number, im:Number
	
	function Complex(re:Number, im:Number){
		this.re = re
		this.im = arguments.length < 2 ? 0 : im
	}
	
	function adjoined(): Complex{
		return new Complex(re, -im)	
	}
	function plus(c: Complex): Complex{
		return new Complex(re + c.re, im + c.im)	
	}
	function minus(c: Complex): Complex{
		return new Complex(re - c.re, im - c.im)	
	}
	function mul(c: Complex): Complex{
		return new Complex(re * c.re - im * c.im, re * c.im + c.re * im)
	}
	function div(c: Complex): Complex{
		var frac = c.re * c.re + c.im * c.im
		var num = mul(c.adjoined())
		return new Complex(num.re / frac, num.im / frac)	
	}
	function dist(c: Complex): Number{
		return Math.sqrt(Math.pow((c.re - re), 2) + Math.pow((c.im - im), 2))
	}
	
	function toString(): String{
		return "(" + re + ", " + im + ")"
	}
}
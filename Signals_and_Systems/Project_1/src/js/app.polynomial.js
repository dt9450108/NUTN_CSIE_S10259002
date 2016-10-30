function solvePoly3(poly) {
    var res = null;
    if (polyDegree(poly) == 3) {
        res = new Polynomial3(poly);
    } else if (polyDegree(poly) == 2) {
        res = new Polynomial2(poly);
    } else if (polyDegree(poly) == 1) {
        res = new Polynomial1(poly);
    }

    return res;
}

function seperateTerms(eq) {
    var mind = eq[0].delay;
    POLY_X = [];
    POLY_Y = [];

    for (var i = 1; i < eq.length; i++)
        if (mind > eq[i].delay)
            mind = eq[i].delay;
    mind = Math.abs(mind);

    for (var i = 0; i <= mind; i++)
        POLY_X.push(0), POLY_Y.push(0);
    // console.log(POLY_X);
    // console.log(POLY_Y);

    for (var i = 0; i < eq.length; i++)
        if (eq[i].signal == CONST_X)
            POLY_X[mind + eq[i].delay] = eq[i].coef;
        else
            POLY_Y[mind + eq[i].delay] = eq[i].coef * -1;
    POLY_Y[mind] = 1;
    // console.log(POLY_X);
    // console.log(POLY_Y);

    var xmsb = POLY_X[polyDegree(POLY_X)],
        ymsb = POLY_Y[polyDegree(POLY_Y)];
    var k = xmsb / ymsb;
    // console.log(xmsb);
    // console.log(ymsb);
    // console.log(k);
    if (k != 0)
        for (var i = 0; i <= mind; i++)
            POLY_X[i] = POLY_X[i] / xmsb, POLY_Y[i] = POLY_Y[i] / ymsb;
    // console.log(POLY_X);
    // console.log(POLY_Y);
    return k;
}

/**
 * 		One factoring
 */
var Polynomial1 = function(coef) {
    if (coef == undefined || coef == null || coef.length < 2 || coef[1] == 0) {
        return null;
    }
    var self = this;

    // coefficients
    this.a = coef[1];
    this.b = coef[0];
    this.r = [];
    this.roots = function() {
        this.r = [];
        var real = (this.b / this.a) * -1;
        this.r.push(math.complex(real, 0));
    };
    this.roots();
}

/**
 * 		Quadratic factoring
 */
var Polynomial2 = function(coef) {
    if (coef == undefined || coef == null || coef.length < 3 || coef[2] == 0) {
        return null;
    }
    var self = this;

    // coefficients
    this.a = coef[2];
    this.b = coef[1];
    this.c = coef[0];
    this.r = [];
    this.roots = function() {
        this.r = [];
        var a = this.a,
            b = this.b,
            c = this.c;
        var disc = (b * b) - (4 * a * c);
        if (disc > 0) {
            this.r.push((-1 * b + Math.sqrt(disc)) / (2 * a));
            this.r.push((-1 * b - Math.sqrt(disc)) / (2 * a));
        } else if (disc == 0) {
            this.r.push((-1 * b) / (2 * a));
            this.r.push((-1 * b) / (2 * a));
        } else {
            var real = (-1 * b) / (2 * a);
            var imag = Math.sqrt(-1 * disc) / (2 * a);
            this.r.push(math.complex(real, imag));
            this.r.push(math.complex(real, -1 * imag));
        }
    };
    this.roots();
}

/**
 * 		Cubic factoring
 */
var Polynomial3 = function(coef) {
    if (coef == undefined || coef == null || coef.length < 4 || coef[3] == 0) {
        return null;
    }
    var self = this;

    this.a = coef[3];
    this.b = coef[2];
    this.c = coef[1];
    this.d = coef[0];
    this.r = [];

    this.roots = function() {
        this.r = [];
        var a = this.a,
            b = this.b,
            c = this.c,
            d = this.d;
        this.r = calculate(a, b, c, d);
    };
    this.roots();
}

//---------------------------
// cubic root code
// http://www.1728.org/cubic.htm
//---------------------------
function calculate(a, b, c, d) {
    var x1 = 0;
    var x2 = 0;
    var x3 = 0;
    var choice = 0;
    var sign = 0;
    var dans = 0;

    var f = eval(((3 * c) / a) - (((b * b) / (a * a)))) / 3;
    var g = eval((2 * ((b * b * b) / (a * a * a)) - (9 * b * c / (a * a)) + ((27 * (d / a))))) / 27;
    var h = eval(((g * g) / 4) + ((f * f * f) / 27));

    if (h > 0) {
        var m = eval(-(g / 2) + (Math.sqrt(h)));
        var k = 1;
        if (m < 0) k = -1;
        else k = 1;
        var m2 = eval(Math.pow((m * k), (1 / 3)));
        m2 = m2 * k;
        k = 1;
        var n = eval(-(g / 2) - (Math.sqrt(h)));
        if (n < 0) k = -1;
        else k = 1;
        var n2 = eval(Math.pow((n * k), (1 / 3)));
        n2 = n2 * k;
        k = 1;
        x1 = eval((m2 + n2) - (b / (3 * a)));

        var tttt1 = (-1 * (m2 + n2) / 2) - (b / (3 * a));
        var tttt2 = ((m2 - n2) / 2) * Math.pow(3, .5);
        x2 = math.complex(tttt1, tttt2);
        x3 = math.complex(tttt1, -1 * tttt2);
    }

    if (h <= 0) {
        r = (eval(Math.sqrt((g * g / 4) - h)));
        k = 1;
        if (r < 0) k = -1;
        rc = Math.pow((r * k), (1 / 3)) * k;
        k = 1;
        theta = Math.acos((-g / (2 * r)));
        x1 = eval(2 * (rc * Math.cos(theta / 3)) - (b / (3 * a)));
        x2a = rc * -1;
        x2b = Math.cos(theta / 3);
        x2c = Math.sqrt(3) * (Math.sin(theta / 3));
        x2d = (b / 3 * a) * -1;
        x2 = eval(x2a * (x2b + x2c)) - (b / (3 * a));
        x3 = eval(x2a * (x2b - x2c)) - (b / (3 * a));

        x1 = x1 * 1E+14;
        x1 = Math.round(x1);
        x1 = (x1 / 1E+14);
        x2 = x2 * 1E+14;
        x2 = Math.round(x2);
        x2 = (x2 / 1E+14);
        x3 = x3 * 1E+14;
        x3 = Math.round(x3);
        x3 = (x3 / 1E+14);
    }

    if ((f + g + h) == 0) {
        if (d < 0) { sign = -1 };
        if (d >= 0) { sign = 1 };
        if (sign > 0) {
            dans = Math.pow((d / a), (1 / 3));
            dans = dans * -1;
        };
        if (sign < 0) {
            d = d * -1;
            dans = Math.pow((d / a), (1 / 3));
        };
        x1 = dans;
        x2 = dans;
        x3 = dans;
    }
    return [x1, x2, x3];
}

function polyDegree(p) {
    for (var i = p.length - 1; i >= 0; i--)
        if (p[i] != 0) return i;
}

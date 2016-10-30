/**
 *      Regex Formula
 */
var EQUATION_REGEX = /([+-]?)(\(?([+-]?)(\d+?)\/([+-]?)(\d+?)\)?|(\d+?)|(\d+?)\.(\d+?))?([xy]){1}\[n([+-]?)(\d+)?\]/g;
var TERM_REGEX = /([xy]){1}\[n([+-]?)(\d+)?\]/;
var INPUT_SIGNAL_REGEX = /([+-])?([()+\-\*\/\d\.]+)?(ust\(|usp\(|sin\(|cos\(|square\(|sawtooth\(|tripuls\(|exp\()([+\-\d\.]+)?\*?t([+\-\d\.]+)?\)/;
var INPUT_TERM_REGEX = /(ust|usp|sin|cos|square|sawtooth|tripuls|exp)/g;

/**
 *      System Constant
 */
var SYSTEM_DEBUG_ENABLE = 1;
var CONST_X = 0;
var CONST_Y = 1;
var CONST_STATE_TRUE = 1;
var CONST_STATE_FALSE = 0;
var CONST_RIGHT = 1;
var CONST_LEFT = 0;
var NUMBER_PERIOD = 1;
var NUMBER_NVALUE = 0;
var FUNCT_TYPE = {
	'SIN': 1,
	'COS': 2,
	'USP': 3,
	'UST': 4,
	'SQUARE': 5,
	'SAWTOOTH': 6,
	'TRIANGLE': 7,
	'EXP': 8
};
var VISIBLE_SET = 1;
var NONVISIBLE_SET = 0;
var SAMPLING_TYPE = NUMBER_PERIOD;
var SYS_RESPONSE = [];
var COLOR_DATA = {
	'X': '#A5D6A7',
	'H': '#90CAF9',
	'Y': '#ff0000',
	'TH': '#0000ff'
};
var FUNCTION_PERIOD = Math.PI * 2;
var DIAGRAM = {
	'SYS': 1,
	'MAGNITUDE': 2,
	'PHASE': 3
};
var SYSTEM_RESPONSE_DIAGRAM = null;
var FR_MAGNITUDE = null;
var FR_PHASE = null;

function uStep(x, A) {
	return x < 0 ? 0 : A;
}

function uSample(x, A) {
	return x ? 0 : A;
}

function Exponential(x, A) {
	return A * Math.exp(x);
}

/**
 * x: time
 * A: amplifier
 * w: period
 * phi: phase
 *
 * Math.sin(), with radian
 */
function Sine(x, A, w, phi) {
	return A * Math.sin(w * x + phi);
}

function Cosine(x, A, w, phi) {
	return A * Math.cos(w * x + phi);
}

function SquareWave(x, A, w, phi) {
	var duty = 0.5;
	var t = w * x + phi;
	t /= FUNCTION_PERIOD;
	return (t - Math.floor(t) >= duty) ? -1 * A : A;
}

function SawToothWave(x, A, w, phi) {
	var width = 1;
	var t = w * x + phi;
	t = (t / FUNCTION_PERIOD) % 1.0;
	// t = math.mod(t / FUNCTION_PERIOD, 1);
	return (t < width) ? (A * (2 * t / width - 1)) : 0;
}

function TriangleWave(x, A, w, phi) {
	var width = 1,
		skew = 0,
		peak = skew * width / 2;
	var t = w * x + phi;
	t = (t / FUNCTION_PERIOD) % 1.0;
	// t = math.mod(t / FUNCTION_PERIOD, 1);
	t -= 0.5;
	if (t >= -1 * width / 2 && t <= peak) return A * ((t + width / 2) / (peak + width / 2));
	if (t > peak && t < width / 2) return A * ((t - width / 2) / (peak - width / 2));
}

function createChart(target, title, w, h, lbx, lby, grid = true, annotations = null) {
	return functionPlot({
		'title': title,
		'width': w,
		'height': h,
		'target': target,
		'xAxis': {
			'label': lbx
		},
		'yAxis': {
			'label': lby
		},
		'grid': grid,
		'data': [],
		'annotations': annotations
	});
}

function addDataToChart(data, xDomain, yDomain, color, diagram) {
	if (diagram != null) {
		var graphType, attr;
		if (diagram == SYSTEM_RESPONSE_DIAGRAM) {
			graphType = "scatter";
			attr = {
				'r': 4,
				'fill': color,
				'stroke': color
			};
		} else {
			graphType = "polyline";
			attr = {
				'stroke': color
			};
		}
		diagram.options.data.push({
			'points': data,
			'fnType': 'points',
			'graphType': graphType,
			'attr': attr
		});
		diagram.draw();
		diagram.programmaticZoom(xDomain, yDomain);
	}
}

function clearDataFromChart(diagram) {
	switch (diagram) {
		case DIAGRAM.SYS:
			{
				var options = SYSTEM_RESPONSE_DIAGRAM.options;
				if (SYSTEM_RESPONSE_DIAGRAM != null && SYSTEM_RESPONSE_DIAGRAM.options.data.length > 0) {
					$(options.target).html("");
					SYSTEM_RESPONSE_DIAGRAM = createChart(
						options.target,
						options.title,
						options.width,
						options.height,
						options.xAxis.label,
						options.yAxis.label
					);
				}
				break;
			}
		case DIAGRAM.MAGNITUDE:
			{
				var options = FR_MAGNITUDE.options;
				if (FR_MAGNITUDE != null && FR_MAGNITUDE.options.data.length > 0) {
					$(options.target).html("");
					FR_MAGNITUDE = createChart(
						options.target,
						options.title,
						options.width,
						options.height,
						options.xAxis.label,
						options.yAxis.label
					);
				}
				break;
			}
		case DIAGRAM.PHASE:
			{
				var options = FR_PHASE.options;
				if (FR_PHASE != null && FR_PHASE.options.data.length > 0) {
					$(options.target).html("");
					FR_PHASE = createChart(
						options.target,
						options.title,
						options.width,
						options.height,
						options.xAxis.label,
						options.yAxis.label,
						options.grid,
						options.annotations
					);
				}
				break;
			}
		default:
			__Debug("clearDataFromChart()", "unhandled diagram");
	}
}

function sTerm(coef = 0, funct = 0, freq = 1, phase = 0) {
	return {
		'coef': coef,
		'funct': funct,
		'freq': freq,
		'phase': phase
	};
}

function dTerm(coef, delay, signal) {
	return {
		'coef': coef,
		'delay': delay,
		'signal': signal
	};
}

function Vitem(state, val) {
	return {
		'state': state,
		'val': val
	};
}

function ErrorMsgBottom(id, title, content, visible) {
	var $err = $("#" + id);
	if (visible) {
		$err.html("<strong>" + title + ": </strong> " + content + "<br>");
		$err.show();
	} else {
		$err.html("");
		$err.hide();
	}
}

function __Debug(fname, content) {
	if (SYSTEM_DEBUG_ENABLE) {
		console.log("%cFunction Name: " + fname, "background: #BBDEFB; color: #212121");
		console.log(content);
	}
}

function highlightText(id, s, e) {
	var input = document.getElementById(id);
	input.setSelectionRange(s, e); // Highlights "Cup"
	input.focus();
}

String.prototype.last = function() {
	return this[this.length - 1];
}

Array.prototype.addFirst = function(e) {
	var t = [e];
	for (var i = 0; i < this.length; i++)
		t.push(this[i]);
	return t;
}

Array.prototype.last = function() {
	return this[this.length - 1];
}

function createInputSignalPanel() {
	var panel = '<div id="InputSignalFloatPanel" class="panel panel-primary"><div class="panel-heading"><div class="panel-title"><h4>系統輸入訊號說明</h4></div>' +
		'</div><div class="panel-body"><div class="row"><div class="col-md-3"></div><div class="col-md-4">' +
		'<h5 class="text-center p-vertial-center"><strong>Symbol</strong></h5></div><div class="col-md-5">' +
		'<h5 class="text-center p-vertial-center"><strong>Meaning</strong></h5></div></div><div class="row">' +
		'<div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Unit-Step</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*ust(at+b)</p></div><div class="col-md-5">' +
		'<p>ust(t) = 1 as t >= 0<br>ust(t) = 0 as t < 0<br>A, a and b are Real number.</p></div></div><div class="row">' +
		'<div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Unit-Sample</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*usp(at+b)</p></div><div class="col-md-5">' +
		'<p>usp(t) = infinity as t = 0<br>usp(t) = 0 as t is not equal to 0<br>A, a and b are Real number.</p></div></div>' +
		'<div class="row"><div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Sine</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*sin(wt + phi)</p></div><div class="col-md-5">' +
		'<p>w is angular frequency<br>phi is phase angle<br>A, w and phi are Real number.</p></div></div>' +
		'<div class="row"><div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Cosine</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*cos(wt + phi)</p></div><div class="col-md-5">' +
		'<p>w is angular frequency<br>phi is phase angle<br>A, w and phi are Real number.</p></div></div><div class="row">' +
		'<div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Square</strong></h5></div><div class="col-md-4">' +
		'<p class="text-center p-vertial-center">A*square(wt + phi)</p></div><div class="col-md-5">' +
		'<p>This is a square wave with the period of two times of pi, and A, w and phi are Real number</p></div></div>' +
		'<div class="row"><div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Sawtooth</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*sawtooth(wt + phi)</p></div><div class="col-md-5">' +
		'<p>This is a sawtooth wave with the period of two times of pi, and A, w and phi are Real number</p></div></div>' +
		'<div class="row"><div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Tripuls</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*tripuls(wt + phi)</p></div><div class="col-md-5">' +
		'<p>This is a triangle wave with the period of two times of pi, and A, w and phi are Real number</p></div></div>' +
		'<div class="row"><div class="col-md-3"><h5 class="text-center p-vertial-center"><strong>Natural Exp.</strong></h5></div>' +
		'<div class="col-md-4"><p class="text-center p-vertial-center">A*exp(at + b)</p></div>' +
		'<div class="col-md-5"><p>A, a and b are Real number</p></div></div></div></div>';
	$("body").append(panel);
	$('#InputSignalFloatPanel').lobiPanel({
		state: "unpinned",
		reload: false,
		editTitle: false,
		unpin: false,
		minWidth: 300,
		minHeight: 300
	});
}

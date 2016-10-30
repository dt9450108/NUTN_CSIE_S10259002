$(document).ready(function() {
	$(".page-header").click(function() {
		SYSTEM_DEBUG_ENABLE = !SYSTEM_DEBUG_ENABLE;
		if (SYSTEM_DEBUG_ENABLE) console.log("%c除錯模式%c「開啟」", "font-size: 15pt;", "color: #d32f2f; font-size: 15pt;");
		else console.log("%c除錯模式%c「關閉」", "font-size: 15pt;", "color: #1976D2; font-size: 15pt;");
	});

	SYSTEM_RESPONSE_DIAGRAM = createChart('#SystemResponse', '', 1000, 400, 'n', 'y[n]');
	FR_MAGNITUDE = createChart('#frMagnitude', 'Magnitude Response', 600, 400, 'logw', 'Magnitude');
	FR_PHASE = createChart('#frPhase', 'Phase Response', 600, 400, 'logw', 'Phase(rad)', false, [{
		y: Math.PI / 2,
		text: 'y = π/2'
	}, {
		y: -1 * Math.PI / 2,
		text: 'y = -π/2'
	}, {
		y: Math.PI,
		text: 'y = π'
	}, {
		y: -1 * Math.PI,
		text: 'y = -π'
	}]);

	$('#ScrollSampleRate, #InputSampleRate').on('mousewheel', function(event) {
		var val = $("#InputSampleRate").val();
		var n = (val == "" || isNaN(val)) ? 0 : Number(val);
		if (n + event.deltaY * 0.01 > 0) $("#InputSampleRate").val((n + event.deltaY * 0.01).toFixed(2));
	});

	$('#ScrollPeriodNumberValue, #InputPeriodNumber').on('mousewheel', function(event) {
		var val = $("#InputPeriodNumber").val();
		var n = (val == "" || isNaN(val)) ? 1 : Number(val);
		if (n + event.deltaY > 0) $("#InputPeriodNumber").val(n + event.deltaY);
	});

	// input signal tooltip
	var InputSignalPanelOpen = false;
	$("#InputSignalTip").click(function(e) {
		if (!InputSignalPanelOpen) {
			createInputSignalPanel();

			var height = $("#InputSignalTip").height();
			var pos = $("#InputSignalTip").offset();
			var ww = $(document).width();
			var x = ww - 620;
			var y = Math.floor(pos.top + height + 20);

			$(".lobipanel").lobiPanel("setPosition", x, y);
			$(".lobipanel").lobiPanel("setSize", 600, 400);
			$('.lobipanel').on('beforeClose.lobiPanel', function(ev, lobiPanel) {
				InputSignalPanelOpen = false;
			});
			setTimeout(function() {
				$("#InputSignalFloatPanel").show();
			}, 300);
			InputSignalPanelOpen = true;
		}
	});

	$(document).on('mouseup', function() {
		$(document).off("mousemove");
	});

	// input signal processing
	$("#InputSignalEquation").on("keyup", function() {
		var input = $(this).val();
		// if (input.indexOf("sin") >= 0 || input.indexOf("cos") >= 0 || input.indexOf("cos") >= 0) {
		if (/(sin|cos|square|sawtooth|tripuls)/.test(input)) {
			$("#InputPeriodOrNvalue").html("週期數(T)");
			$("#InputPeriodNumber").val(1);
			SAMPLING_TYPE = NUMBER_PERIOD;
		} else {
			$("#InputPeriodOrNvalue").html("取樣點數(N)");
			$("#InputPeriodNumber").val(50);
			SAMPLING_TYPE = NUMBER_NVALUE;
		}
	});

	// start to calculate button
	$("#SetEquation").click(calculate);


	// clear input tag of sampling rate
	$("#ResetSampleRate").click(function() {
		$("#InputSampleRate").val("10");
	});

	// clear input tag of number of period for sampling
	$("#ResetPeriodNumber").click(function() {
		$("#InputPeriodNumber").val("1");
		$("#InputPeriodNumber").focus();
		$("#InputPeriodOrNvalue").html("週期數(T)");
	});

	// clear the input signal of the system
	$("#ResetInputSignal").click(function() {
		$("#InputSignalEquation").val("");
		$("#InputSignalEquation").focus();
		$("#InputPeriodOrNvalue").html("週期數(T)");
		$("#InputPeriodNumber").val("1");
	});

	// clear the system (transfer function)
	$("#ResetTransferFunction").click(function() {
		$("#InputTransferFunction").val("");
		$("#InputTransferFunction").focus();
	});

	// reset the system response of the arbitrary input signal
	$("#ResetSystemResponse").click(function() {
		clearDataFromChart(DIAGRAM.SYS);
	});
	$("#ResetFrMagnitude").click(function() {
		clearDataFromChart(DIAGRAM.MAGNITUDE);
	});
	$("#ResetFrPhase").click(function() {
		clearDataFromChart(DIAGRAM.PHASE);
	});

	// bootstrap tooltip
	$('[data-toggle="tooltip"]').tooltip();
});

function calculate() {
	// new calculation, thus clear the chart data
	clearDataFromChart(DIAGRAM.SYS);
	clearDataFromChart(DIAGRAM.MAGNITUDE);
	clearDataFromChart(DIAGRAM.PHASE);

	var InputValidation = true;
	// sampling the input signal
	var sRate = $("#InputSampleRate").val().replace(/ /g, ''),
		sPeriod = $("#InputPeriodNumber").val().replace(/ /g, '');
	if (sRate == "" || sRate == null) {
		ErrorMsgBottom("InputSampleRateValueErrorMsg", "Error", "請輸入「取樣頻率」", VISIBLE_SET);
		InputValidation = false;
	} else {
		if (!isNaN(sRate) && sRate != "0") {
			ErrorMsgBottom("InputSampleRateValueErrorMsg", null, null, NONVISIBLE_SET);
			// sRate = sRate % 101;
		} else {
			ErrorMsgBottom("InputSampleRateValueErrorMsg", "Error", "「取樣頻率」需為非零正數", VISIBLE_SET);
			InputValidation = false;
		}
	}

	// log w bound check
	var logw = {
		'low': $("#InputLogwLower").val(),
		'up': $("#InputLogwUpper").val()
	};
	if (logw.low == "" || logw.low == null) {
		ErrorMsgBottom("InputLogwErrorMsg", "Error", "logw「下限」輸入錯誤", VISIBLE_SET);
		InputValidation = false;
	} else if (logw.up == "" || logw.up == null) {
		ErrorMsgBottom("InputLogwErrorMsg", "Error", "logw「上限」輸入錯誤", VISIBLE_SET);
		InputValidation = false;
	} else {
		if (!/^[+\-\d]+$/.test(logw.up) || !/^[+\-\d]+$/.test(logw.low)) {
			ErrorMsgBottom("InputLogwErrorMsg", "Error", "logw「下限」或「上限」不為整數", VISIBLE_SET);
			InputValidation = false;
		} else {
			ErrorMsgBottom("InputLogwErrorMsg", null, null, NONVISIBLE_SET);
			logw.low = parseInt(logw.low);
			logw.up = parseInt(logw.up);
			if (logw.low >= logw.up) {
				ErrorMsgBottom("InputLogwErrorMsg", "Error", "logw「下限」超過「上限」", VISIBLE_SET);
				InputValidation = false;
			} else {
				ErrorMsgBottom("InputLogwErrorMsg", null, null, NONVISIBLE_SET);
				if (logw.low < -20) logw.low = -20;
				if (logw.up > 20) logw.up = 20;
				$("#InputLogwLower").val(logw.low);
				$("#InputLogwUpper").val(logw.up);
			}
		}
	}

	if (sPeriod == "" || sPeriod == null) {
		ErrorMsgBottom("InputPeriodNumberValueErrorMsg", "Error", (SAMPLING_TYPE == NUMBER_PERIOD ? "請輸入「週期數」" : "請輸入「取樣點數」"), VISIBLE_SET);
		InputValidation = false;
	} else ErrorMsgBottom("InputPeriodNumberValueErrorMsg", null, null, NONVISIBLE_SET);

	// get the input signal string from input tag
	var signalEquationStr = $("#InputSignalEquation").val(),
		inputSignal;
	if (signalEquationStr != "" && signalEquationStr != null) {
		ErrorMsgBottom("InputSignalErrorMsg", null, null, NONVISIBLE_SET);
		signalEquationStr = signalEquationStr.toLowerCase().replace(/ /g, '');
		$("#InputSignalEquation").val(signalEquationStr);

		var eqRes = InputSignalParser(signalEquationStr);
		if (eqRes.state == CONST_STATE_FALSE) {
			highlightText("InputSignalEquation", eqRes.val.val.s, eqRes.val.val.e);
			ErrorMsgBottom("InputSignalErrorMsg", "Error", eqRes.val.msg, VISIBLE_SET);
			InputValidation = false;
		} else {
			ErrorMsgBottom("InputSignalErrorMsg", null, null, NONVISIBLE_SET);
			inputSignal = eqRes;
			__Debug("calculate : inputSignal", inputSignal);
		}
	} else {
		ErrorMsgBottom("InputSignalErrorMsg", "Error", "請輸入「系統輸入訊號」", VISIBLE_SET);
		InputValidation = false;
	}

	// get the difference equation of the system
	var diffEquationStr = $("#InputTransferFunction").val(),
		diffEquation, transferFunct;
	if (diffEquationStr != "" && diffEquationStr != null) {
		ErrorMsgBottom("InputTransferFunctionErrorMsg", null, null, NONVISIBLE_SET);
		diffEquationStr = diffEquationStr.toLowerCase().replace(/ /g, '');
		$("#InputTransferFunction").val(diffEquationStr);

		var eqRes = DiffEqParser(diffEquationStr);
		if (eqRes.state == CONST_STATE_FALSE) {
			var errStr = eqRes.val.val;
			var s = diffEquationStr.indexOf(errStr);
			var e = s + errStr.length;
			highlightText("InputTransferFunction", s, e);
			ErrorMsgBottom("InputTransferFunctionErrorMsg", "Error", eqRes.val.msg, VISIBLE_SET);
			InputValidation = false;
		} else {
			ErrorMsgBottom("InputTransferFunctionErrorMsg", null, null, NONVISIBLE_SET);
			diffEquation = eqRes;
			__Debug("calculate : diffEquation", diffEquation);

			var tf = seperateTerms(eqRes);
			var num = tf.num,
				den = tf.den,
				numRoots = [],
				denRoots = [];
			if (tf.num.length > 1) {
				var c = [];
				c.push(tf.num.length - 1);
				for (var i = tf.num.length - 1; i >= 0; i--) c.push(tf.num[i]);
				numRoots = PolyReSolveT(c);
			}

			if (tf.den.length > 1) {
				var c = [];
				c.push(tf.den.length - 1);
				for (var i = tf.den.length - 1; i >= 0; i--) c.push(tf.den[i]);
				denRoots = PolyReSolveT(c);
			}
			transferFunct = {
				'k': tf.k,
				'zeros': numRoots,
				'poles': denRoots
			};
		}
	} else {
		ErrorMsgBottom("InputTransferFunctionErrorMsg", "Error", "請輸入「系統差分方程式」", VISIBLE_SET);
		InputValidation = false;
	}

	if (InputValidation) {
		var x = samplingInputSignal(inputSignal, sRate, sPeriod);
		var h = samplingSystemEquation(diffEquation, sRate, sPeriod);
		var y = convolution(x.d, h.d);
		addDataToChart(x.data, [-1, x.d.length], [Math.min.apply(null, x.d) - 1, Math.max.apply(null, x.d) + 1], COLOR_DATA.X, SYSTEM_RESPONSE_DIAGRAM);
		addDataToChart(h.data, [-1, h.d.length], [Math.min.apply(null, h.d) - 1, Math.max.apply(null, h.d) + 1], COLOR_DATA.H, SYSTEM_RESPONSE_DIAGRAM);
		addDataToChart(y.data, [-1, y.d.length], [Math.min.apply(null, y.d) - 1, Math.max.apply(null, y.d) + 1], COLOR_DATA.Y, SYSTEM_RESPONSE_DIAGRAM);
		// frequency response
		var frMagnData = [],
			frMagn = [],
			frPhaseData = [],
			frPhase = [],
			frThMagnData = [],
			frThMagn = [],
			frThPhaseData = [],
			ftThPhase = [];
		var xdomain = [logw.low - 1, logw.up + 1];
		for (var i = xdomain[0] + 1; i <= xdomain[1] - 1; i++) {
			setTimeout(function(i) {
				var sign = i >= 0 ? 1 : -1,
					w = Math.pow(10, Math.abs(i)) * sign,
					frInputSignal = [new sTerm(1, FUNCT_TYPE.SIN, w, 0)],
					frx = samplingInputSignal(frInputSignal, sRate, sPeriod),
					fry = convolution(frx.d, h.d),
					magnitude = Math.max.apply(null, fry.d.map(Math.abs)),
					phi = getPhi(w, findDelay(frx.d, fry.d), 1 / sRate),
					theoryRes = evalFrequencyResponse(transferFunct, w);
				frMagnData.push([i, magnitude]);
				frMagn.push(magnitude);
				frPhaseData.push([i, phi]);
				frPhase.push(phi);
				frThMagnData.push([i, theoryRes.magnitude]);
				frThMagn.push(theoryRes.magnitude);
				frThPhaseData.push([i, theoryRes.phase]);
				ftThPhase.push(theoryRes.phase);
			}, 0, i);
		}

		// Theory Magnitude frequency response
		setTimeout(function() {
			addDataToChart(frThMagnData, xdomain, [Math.min.apply(null, frThMagn) - 1, Math.max.apply(null, frThMagn) + 1], COLOR_DATA.TH, FR_MAGNITUDE);
		}, 1000);

		// Theory Phase frequency response
		setTimeout(function() {
			addDataToChart(frThPhaseData, xdomain, [Math.min.apply(null, ftThPhase) - 1, Math.max.apply(null, ftThPhase) + 1], COLOR_DATA.TH, FR_PHASE);
		}, 2000);

		// Magnitude frequency response
		setTimeout(function() {
			addDataToChart(frMagnData, xdomain, [Math.min.apply(null, frMagn) - 1, Math.max.apply(null, frMagn) + 1], COLOR_DATA.Y, FR_MAGNITUDE);
		}, 3000);

		// Phase frequency response
		setTimeout(function() {
			addDataToChart(frPhaseData, xdomain, [Math.min.apply(null, frPhase) - 1, Math.max.apply(null, frPhase) + 1], COLOR_DATA.Y, FR_PHASE);
		}, 4000);
	}
}

function evalFrequencyResponse(tf, w) {
	var magnitude = Math.abs(tf.k);
	var phase = tf.k >= 0 ? 0 : -1 * Math.PI;

	for (var i = 0; i < tf.zeros.length; i++) {
		var cterm = math.subtract(math.complex(0, w), tf.zeros[i]);
		magnitude += complexMagnitude(cterm);
		phase += complexPhi(cterm);
	}
	for (var i = 0; i < tf.poles.length; i++) {
		var cterm = math.subtract(math.complex(0, w), tf.poles[i]);
		magnitude -= complexMagnitude(cterm);
		phase -= complexPhi(cterm);
	}
	return {
		'magnitude': Math.abs(magnitude),
		'phase': phase
	};
}

function complexMagnitude(c) {
	return Math.sqrt(c.re * c.re + c.im * c.im);
}

function complexPhi(c) {
	return Math.atan(c.im / c.re);
}

function getPhi(w, d, ts) {
	var absw = Math.abs(w),
		signw = Math.sign(w),
		phi = (absw * d * ts) % Math.PI;
	return (phi > (Math.PI * 0.5)) ? (signw * (phi - (Math.PI * 0.5))) : (signw * phi);
}

function findDelay(x, y) {
	var xd = x.indexOf(Math.max.apply(null, x)),
		yd = y.indexOf(Math.max.apply(null, y));
	return Math.abs(xd - yd);
}

function convolution(x, h) {
	var y = [],
		data = [];
	for (var n = 0; n < x.length + h.length - 1; n++) {
		y.push(0);
		var kmin = (n >= h.length - 1) ? n - (h.length - 1) : 0,
			kmax = (n < x.length - 1) ? n : x.length - 1;
		for (var k = kmin; k <= kmax; k++)
			y[n] += x[k] * h[n - k];
		data.push([n, y[n]]);
	}
	return {
		'data': data,
		'd': y
	};
}

function samplingSystemEquation(sysEq, rate, period) {
	var data = [],
		h = [],
		sPeriod = 1 / rate,
		samplingNumber;
	if (SAMPLING_TYPE == NUMBER_PERIOD) samplingNumber = Math.ceil(((2 * period * Math.PI) / sPeriod));
	else samplingNumber = period;

	for (var i = 0; i <= samplingNumber; i++) {
		var yn = 0;
		for (var j = 0; j < sysEq.length; j++) {
			if (sysEq[j].signal == CONST_X) {
				yn += sysEq[j].coef * uSample(i + sysEq[j].delay, 1);
			} else {
				if (i + sysEq[j].delay < 0)
					yn += 0;
				else
					yn += sysEq[j].coef * h[i + sysEq[j].delay];
			}
		}
		data.push([i, yn]);
		h.push(yn);
	}
	return {
		'data': data,
		'd': h
	};
}

function samplingInputSignal(inSignal, rate, period) {
	var data = [],
		x = [],
		sPeriod = 1 / rate,
		samplingNumber;
	if (SAMPLING_TYPE == NUMBER_PERIOD) samplingNumber = Math.ceil(((2 * period * Math.PI) / sPeriod));
	else samplingNumber = period;

	for (var i = 0; i <= samplingNumber; i++) {
		var tmpValue = 0;
		for (var j = 0; j < inSignal.length; j++) {
			switch (inSignal[j].funct) {
				case FUNCT_TYPE.SIN:
					tmpValue += Sine(i * sPeriod, inSignal[j].coef, inSignal[j].freq, inSignal[j].phase);
					break;
				case FUNCT_TYPE.COS:
					tmpValue += Cosine(i * sPeriod, inSignal[j].coef, inSignal[j].freq, inSignal[j].phase);
					break;
				case FUNCT_TYPE.USP:
					tmpValue += uSample((inSignal[j].freq * i + inSignal[j].phase), inSignal[j].coef);
					break;
				case FUNCT_TYPE.UST:
					tmpValue += uStep((inSignal[j].freq * i + inSignal[j].phase), inSignal[j].coef);
					break;
				case FUNCT_TYPE.SQUARE:
					tmpValue += SquareWave(i * sPeriod, inSignal[j].coef, inSignal[j].freq, inSignal[j].phase);
					break;
				case FUNCT_TYPE.SAWTOOTH:
					tmpValue += SawToothWave(i * sPeriod, inSignal[j].coef, inSignal[j].freq, inSignal[j].phase);
					break;
				case FUNCT_TYPE.TRIANGLE:
					tmpValue += TriangleWave(i * sPeriod, inSignal[j].coef, inSignal[j].freq, inSignal[j].phase);
					break;
				case FUNCT_TYPE.EXP:
					tmpValue += Exponential((inSignal[j].freq * i + inSignal[j].phase), inSignal[j].coef);
					break;
				default:
					__Debug("samplingInputSignal()", "unhandled FUNCT_TYPE : " + inSignal[j].funct);
			}
		}
		x.push(tmpValue);
		data.push([i, tmpValue]);
	}
	return {
		'data': data,
		'd': x
	};
}

function InputSignalParser(input) {
	// find the position of the input signal which the function is
	var termIdx = [],
		leftIdx = [],
		rightIdx = [],
		termRes;
	while ((termRes = INPUT_TERM_REGEX.exec(input)) !== null) {
		if (termRes.index === INPUT_TERM_REGEX.lastIndex) INPUT_TERM_REGEX.lastIndex++;
		termIdx.push(termRes.index);
		var idx = input.indexOf('(', termRes.index);
		if (idx != -1) leftIdx.push(idx);
		idx = input.indexOf(')', termRes.index);
		if (idx != -1) rightIdx.push(idx);
	}

	if (termIdx.length != leftIdx.length || leftIdx.length != rightIdx.length || termIdx.length == 0)
		return new Vitem(CONST_STATE_FALSE, {
			'val': {
				's': 0,
				'e': input.length
			},
			'msg': "輸入訊號有錯誤，請檢查！"
		});

	// split each term by usp, ust, sin or cos
	var termItem = [];
	var strStart = 0;
	for (var i = 0; i < termIdx.length; i++) {
		var termPos = termIdx[i],
			leftPara = leftIdx[i],
			rightPara = rightIdx[i];

		if (termPos < leftPara && leftPara < rightPara) {
			termItem.push(input.substring(strStart, rightPara + 1));
			strStart = rightPara + 1;
		} else {
			__Debug("InputSignalParser()", "parenthesis error: " + input);
			return new Vitem(CONST_STATE_FALSE, {
				'val': {
					's': i,
					'e': rightPara + 1
				},
				'msg': "輸入訊號括弧不正確，請檢查！"
			});
		}

		if (strStart < input.length)
			if (!/[+-]/.test(input[strStart])) {
				__Debug("InputSignalParser()", "sign error: " + input);
				return new Vitem(CONST_STATE_FALSE, {
					'val': {
						's': strStart,
						'e': input.length
					},
					'msg': "輸入訊號不得相乘或相除，請檢查！"
				});
			}
	}
	// check coefficient
	var ccstart = 0;
	for (var i = 0; i < termIdx.length; i++) {
		var str = input.substring(ccstart, termIdx[i]);
		if (str.length != 1)
			try {
				math.eval(str);
			} catch (e) {
				return new Vitem(CONST_STATE_FALSE, {
					'val': {
						's': ccstart,
						'e': termIdx[i]
					},
					'msg': "此「" + input.substring(termIdx[i], rightIdx[i] + 1) + "」項係數錯誤"
				});
			}
		ccstart = rightIdx[i] + 1;
	}

	// parsing each termItem
	var parsedTerm = [];
	for (var i = 0; i < termItem.length; i++) {
		var regRes;
		if ((regRes = INPUT_SIGNAL_REGEX.exec(termItem[i])) != null) {
			var obj = new sTerm();

			// determine sign
			var sign = 1;
			if (regRes[1] !== undefined && regRes[1] == "-") sign = -1;

			// determine coef
			__Debug("InputSignalParser Coefficient", regRes[2]);
			__Debug("InputSignalParser Match Result", regRes);
			if (regRes[2] === undefined) obj.coef = sign;
			else {
				if (regRes[2].length === 1 && /[\*\/]/.test(regRes[2].last())) {
					return new Vitem(CONST_STATE_FALSE, {
						'val': {
							's': termIdx[i],
							'e': rightIdx[i] + 1
						},
						'msg': "此 " + input.substring(termIdx[i], rightIdx[i] + 1) + " 的係數錯誤，請檢查！"
					});
				} else {
					try {
						if (regRes[2].last() === "*") obj.coef = math.eval(regRes[2].substr(0, regRes[2].length - 1)) * sign;
						else obj.coef = math.eval(regRes[2]) * sign;
					} catch (e) {
						return new Vitem(CONST_STATE_FALSE, {
							'val': {
								's': termIdx[i],
								'e': rightIdx[i] + 1
							},
							'msg': "此 " + input.substring(termIdx[i], rightIdx[i] + 1) + " 的係數錯誤，請檢查！"
						});
					}
				}
			}

			// determine the function type which are ust, usp, sin or cos
			if (regRes[3] == "sin(") obj.funct = FUNCT_TYPE.SIN;
			else if (regRes[3] == "cos(") obj.funct = FUNCT_TYPE.COS;
			else if (regRes[3] == "ust(") obj.funct = FUNCT_TYPE.UST;
			else if (regRes[3] == "usp(") obj.funct = FUNCT_TYPE.USP;
			else if (regRes[3] == "square(") obj.funct = FUNCT_TYPE.SQUARE;
			else if (regRes[3] == "sawtooth(") obj.funct = FUNCT_TYPE.SAWTOOTH;
			else if (regRes[3] == "tripuls(") obj.funct = FUNCT_TYPE.TRIANGLE;
			else if (regRes[3] == "exp(") obj.funct = FUNCT_TYPE.EXP;

			// determine the w which is the angular frequency
			if (regRes[4] !== undefined) {
				if (regRes[4].length === 1 && regRes[4] === "-") obj.freq = -1;
				else if (regRes[4].length === 1 && regRes[4] === "0") {
					return new Vitem(CONST_STATE_FALSE, {
						'val': {
							's': termIdx[tn],
							'e': rightIdx[tn] + 1
						},
						'msg': "「角頻率」不得為零，請檢查！"
					});
				} else obj.freq = math.eval(regRes[4]);
			}

			// determine the phi which is the phase angle
			if (regRes[5] !== undefined) {
				if (regRes[5].length === 1 && /[\-+]/.test(regRes[5])) {
					return new Vitem(CONST_STATE_FALSE, {
						'val': {
							's': termIdx[tn],
							'e': rightIdx[tn] + 1
						},
						'msg': "輸入訊號的「相位角」格式錯誤，請檢查！"
					});
				} else
					try {
						obj.phase = math.eval(regRes[5]);
					} catch (e) {
						__Debug("InputSignalParser", "parsing phi error : " + regRes[5]);
						return new Vitem(CONST_STATE_FALSE, {
							'val': {
								's': termIdx[tn],
								'e': rightIdx[tn] + 1
							},
							'msg': "輸入訊號的「相位角」格式錯誤，請檢查！"
						});
					}
			}
			parsedTerm.push(obj);
		} else {
			__Debug("InputSignalParser()", "regex error: " + termItem[i]);
			var s = input.indexOf(termItem[i]);
			return new Vitem(CONST_STATE_FALSE, {
				'val': {
					's': s,
					'e': s + termItem[i].length
				},
				'msg': "輸入訊號格式錯誤，請檢查！"
			});
		}
	}

	// integrate same termItem
	var inputEq = [];
	var nt;
	for (var i = 0; i < parsedTerm.length; i++) {
		nt = parsedTerm[i];
		for (var j = i + 1; j < parsedTerm.length; j++) {
			if (nt.funct == parsedTerm[j].funct && nt.freq == parsedTerm[j].freq && nt.phase == parsedTerm[j].phase) {
				nt.coef += parsedTerm[j].coef;
				parsedTerm.splice(j, 1);
			}
		}
		if (nt.coef != 0) inputEq.push(nt);
	}
	return inputEq;
}

function DiffEqParser(str) {
	if (str.indexOf('=') != -1)
		return new Vitem(CONST_STATE_FALSE, {
			'val': '=',
			'msg': "系統之差分方程式有誤，請檢查！"
		});
	else if (str.indexOf("y[n]") != -1)
		return new Vitem(CONST_STATE_FALSE, {
			'val': 'y[n]',
			'msg': "系統之差分方程式有誤，請檢查！"
		});
	else if (str.indexOf("y[n-0]") != -1)
		return new Vitem(CONST_STATE_FALSE, {
			'val': 'y[n-0]',
			'msg': "系統之差分方程式有誤，請檢查！"
		});
	else if (str.indexOf("y[n+0]") != -1)
		return new Vitem(CONST_STATE_FALSE, {
			'val': 'y[n+0]',
			'msg': "系統之差分方程式有誤，請檢查！"
		});

	var termIdx = [],
		rightIdx = [],
		termPosRegex = new RegExp(/([xy])/g),
		termRes;
	while ((termRes = termPosRegex.exec(str)) !== null) {
		if (termRes.index === termPosRegex.lastIndex) termPosRegex.lastIndex++;
		termIdx.push(termRes.index);
		var idx = str.indexOf(']', termRes.index);
		if (idx != -1) rightIdx.push(idx);
	}

	if (termIdx.length != rightIdx.length || termIdx.length == 0)
		return new Vitem(CONST_STATE_FALSE, {
			'val': str,
			'msg': "系統之差分方程式有誤，請檢查！"
		});

	var tn = 0,
		eq = [];
	var coef, coefStr, tpr, delay, signal;
	for (var i = 0; i < str.length && tn < termIdx.length;) {
		// check invalid
		// coefficient must be at left of x or y term
		if (tn < termIdx.length - 1) {
			if (str[rightIdx[tn] + 1] != '+' && str[rightIdx[tn] + 1] != '-') {
				__Debug("DiffEqParser", {
					'msg': 'Coefficient is not at left of previous x or y term.',
					'term': str.substring(termIdx[tn], rightIdx[tn] + 1)
				});
				return new Vitem(CONST_STATE_FALSE, {
					'val': str.substring(termIdx[tn], rightIdx[tn] + 1),
					'msg': "係數必須要在x和y項的左邊，此" + str.substring(termIdx[tn], rightIdx[tn] + 1) + "項有誤，請檢查！"
				});
			}
		} else {
			if (rightIdx[tn] != str.length - 1) {
				__Debug("DiffEqParser", {
					'msg': 'Coefficient is not at left of last x or y term.',
					'term': str.substring(termIdx[tn], rightIdx[tn] + 1)
				});
				return new Vitem(CONST_STATE_FALSE, {
					'val': str.substring(termIdx[tn], rightIdx[tn] + 1),
					'msg': "係數必須要在x和y項的左邊，此" + str.substring(termIdx[tn], rightIdx[tn] + 1) + "項有誤，請檢查！"
				});
			}
		}

		coefStr = str.substring(i, termIdx[tn]);
		if (coefStr.length == 1 && (coefStr[0] == '+' || coefStr[0] == '-')) coefStr = coefStr + "1";
		else if (tn < 1 && coefStr == "") coefStr = 1;
		else if (coefStr.last() == '*') coefStr = coefStr.substring(0, coefStr.length - 1);

		if (coefStr.length > 1 && (coefStr.last() == '+' || coefStr.last() == '-' || coefStr.last() == '/')) {
			// The last character of the coefficient must not be '+', '-' and '/'
			__Debug("DiffEqParser", {
				'msg': 'Coefficient is wrong at last character.',
				'coef': coefStr
			});
			return new Vitem(CONST_STATE_FALSE, {
				'val': coefStr,
				'msg': str.substring(termIdx[tn], rightIdx[tn] + 1) + "項的係數格式錯誤"
			});
		}

		// check coefficient
		try {
			coef = math.eval(coefStr);
		} catch (e) {
			__Debug("DiffEqParser", {
				'msg': 'Parsing coefficient goes wrong.',
				'coef': coefStr
			});
			return new Vitem(CONST_STATE_FALSE, {
				'val': coefStr,
				'msg': str.substring(termIdx[tn], rightIdx[tn] + 1) + "項的係數格式錯誤"
			});
		}

		tpr = ParseTerm(str.substring(termIdx[tn], rightIdx[tn] + 1));
		if (tpr.state == CONST_STATE_FALSE) {
			__Debug("DiffEqParser", {
				'msg': 'Parsing term is wrong at term, ' + str.substring(termIdx[tn], rightIdx[tn] + 1) + '.',
				'term': str.substring(termIdx[tn], rightIdx[tn] + 1)
			});
			return tpr;
		}

		eq.push(new dTerm(coef, tpr.val.delay, tpr.val.signal));
		i = rightIdx[tn] + 1;
		tn++;
	}

	// integrate terms
	var neq = [];
	var nt;
	for (var i = 0; i < eq.length; i++) {
		nt = eq[i];
		for (var j = i + 1; j < eq.length; j++) {
			if (nt.delay == eq[j].delay && nt.signal == eq[j].signal) {
				nt.coef += eq[j].coef;
				eq.splice(j, 1);
			}
		}
		if (nt.coef != 0) neq.push(nt);
	}

	var nx = 0;
	for (var i = 0; i < neq.length; i++)
		if (neq[i].signal == CONST_X) nx++;
	if (!nx) return new Vitem(CONST_STATE_FALSE, {
		'val': str,
		'msg': "系統之差分方程式未有x項"
	});

	return neq;
}

function ParseTerm(str) {
	var re = new RegExp(TERM_REGEX);
	var parsed;
	if ((parsed = re.exec(str)) === null)
		return new Vitem(CONST_STATE_FALSE, {
			'val': str,
			'msg': str + '項的格式錯誤'
		});

	var signal = (parsed[1] == 'x') ? CONST_X : CONST_Y;
	var sign = (parsed[2] == '-') ? -1 : 1;
	if (parsed[3] == undefined || parsed[3] == "" || parsed[3] == null)
		parsed[3] = "0";
	var delay = sign * Number(parsed[3]);

	if (delay > 0)
		return new Vitem(CONST_STATE_FALSE, {
			'val': str,
			'msg': str + '項會造成nocausal產生'
		});
	return new Vitem(CONST_STATE_TRUE, {
		'delay': delay,
		'signal': signal
	});
}

function getMinDelay(eq) {
	var min = eq[0].delay;
	for (var i = 1; i < eq.length; i++) {
		if (min > eq[i].delay) {
			min = eq[i].delay;
		}
	}
	return min;
}

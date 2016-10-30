/**
 *      Regex Formula
 */
var EQUATION_REGEX = /([+-]?)(\(?([+-]?)(\d+?)\/([+-]?)(\d+?)\)?|(\d+?)|(\d+?)\.(\d+?))?([xy]){1}\[n([+-]?)(\d+)?\]/g;
var TERM_REGEX = /([xy]){1}\[n([+-]?)(\d+)?\]/;

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
var CONST_UNIT_STEP = 0;
var CONST_UNIT_SAMPLE = 1;
var VISIBLE_SET = 1;
var NONVISIBLE_SET = 0;
var N_VALUE = 10;
var SYS_UNIT_STEP_RESPONSE = [];
var SYS_UNIT_SAMPLE_RESPONSE = [];
var SYS_DIFF_EQUATION;
var SYSTEM_UST = null;
var SYSTEM_USP = null;
var OUT_RESPONSE_DATA_LABLE = 'Response';
var INS_USTEP_DATA_LABLE = 'Unit-Step';
var INS_USAMPLE_DATA_LABLE = 'Unit-Sample';
var OLD_SYS_EQSTR = "";


/**
 *          Polynomial
 */
var POLY_X = [];
var POLY_Y = [];

function eqNumPad(input, setCallback) {
    var ccaret = $("#diffeq-pad-input").caret();
    var len = $("#diffeq-pad-input").val().length;

    if (!isNaN(input) && input == -1) {
        if (ccaret != 0) {
            var s = $("#diffeq-pad-input").val();
            $("#diffeq-pad-input").val(s.substring(0, ccaret - 1) + s.substring(ccaret, len));
            $("#diffeq-pad-input").caret(ccaret - 1);
        }
    } else if (!isNaN(input) && input == 10) {
        $("#InputEquation").val($("#diffeq-pad-input").val());
        $("#eqCalculator").modal("hide");
        setTimeout(setCallback, 300);
    } else {
        var s = $("#diffeq-pad-input").val();
        $("#diffeq-pad-input").val(s.substring(0, ccaret) + input + s.substring(ccaret, len));
        $("#diffeq-pad-input").caret(ccaret + 1);
    }

    len = $("#diffeq-pad-input").val().length;
    if (len == 0)
        $("#diffeq-pad-input-clear").css('display', 'none');
    else
        $("#diffeq-pad-input-clear").css('display', 'inline-block');
}

function uStep(x) {
    return x < 0 ? 0 : 1;
}

function uSample(x) {
    return x ? 0 : 1;
}

function Term(coef, delay, signal) {
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

/**
 * [createChart description]
 * @param  {[options]} options {
 *       id: id of div tag
 *       datas: columns data
 *       empty: true or false
 *       type: line, spline, step, area, area-spline, area-step, bar, scatter, pie, donut, gauge
 *       x: {
 *           label: x axis name
 *       }
 *       y: {
 *           label: y axis name
 *       }
 * }
 * @return {[type]}         [description]
 */
function createChart(options) {
    var chartOpt = {
        'bindto': '#' + options.id,
        'point': {
            'r': 8
        },
        'data': {
            'columns': null
        },
        'axis': {
            'x': { 'label': { 'text': options.x.label, 'position': 'outer-center' } },
            'y': { 'label': { 'text': options.y.label, 'position': 'outer-middle' } }
        },
        'zoom': {
            'enabled': true,
            'rescale': true
        },
        'grid': {
            'x': {
                'lines': [
                    { 'value': 0, 'text': '' },
                ],
                'show': true
            },
            'y': {
                'lines': [
                    { 'value': 0, 'text': '' },
                ]
            }
        }
    };

    if (!options.empty) {
        chartOpt.data.type = options.type;
        chartOpt.data.columns = options.datas;
    } else {
        chartOpt.data['empty'] = {
            'label': {
                'text': 'No Data'
            }
        };
        chartOpt.data.columns = [];
    }
    return c3.generate(chartOpt);
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
        console.log("Function Name: " + fname);
        console.log(content);
    }
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

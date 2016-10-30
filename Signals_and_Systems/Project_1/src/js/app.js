$(document).ready(function() {
    // detect device
    if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
        $('#diffeq-pad-input').prop('readonly', true);
    }

    $(".page-header").click(function() {
        SYSTEM_DEBUG_ENABLE = !SYSTEM_DEBUG_ENABLE;
        console.log(SYSTEM_DEBUG_ENABLE);
    });

    SYSTEM_UST = new createChart({
        'id': 'unitStepChart',
        'datas': [],
        'empty': true,
        'x': { 'label': 'n' },
        'y': { 'label': 'y[n]' }
    });

    SYSTEM_USP = new createChart({
        'id': 'unitSampleChart',
        'datas': [],
        'empty': true,
        'x': { 'label': 'n' },
        'y': { 'label': 'y[n]' }
    });

    $('#ScrollNvalue').on('mousewheel', function(event) {
        var n = ($("#InputNvalue").val() == "") ? 0 : Number($("#InputNvalue").val());
        if (n + event.deltaY >= 0)
            $("#InputNvalue").val(n + event.deltaY);
    }).on('mousedown', function() {
        var spc = 0
        $(document).on('mousemove', { 'spc': spc }, function(e) {
            var speed = e.data.spc++;
            var ox = e.originalEvent.movementX,
                oy = e.originalEvent.movementY * -1,
                n = ($("#InputNvalue").val() == "") ? 0 : Number($("#InputNvalue").val()),
                x, y;
            if (!ox || !oy) {
                // console.log(ox + ", " + oy + " | " + e.originalEvent.offsetX + ", " + e.originalEvent.offsetY);
                var m = ox || oy,
                    am = Math.abs(m),
                    nm = (m / am) * Math.ceil(am / 10);

                if (nm && n + nm >= 0 && speed >= 5)
                    $("#InputNvalue").val(n + nm), e.data.spc = 0;
            }
        });
    });
    $(document).on('mouseup', function() {
        $(document).off("mousemove");
    });

    $("#OpenEqNumPad").click(function() {
        $("#diffeq-pad-input-clear").css('display', 'none');
        $("#eqCalculator").modal('show');
        setTimeout(function() {
            $("#diffeq-pad-input-clear").bind("click", function() {
                $("#diffeq-pad-input").val("");
                $("#diffeq-pad-input-clear").css('display', 'none');
            });

            $("#diffeq-pad-input").on("propertychange change click keyup input paste", function() {
                if ($("#diffeq-pad-input").val().length == 0) {
                    $("#diffeq-pad-input-clear").css('display', 'none');
                } else {
                    $("#diffeq-pad-input-clear").css('display', 'inline-block');
                }
            });

            if ($("#InputEquation").val() != "") {
                $("#diffeq-pad-input").val($("#InputEquation").val());
                $("#diffeq-pad-input-clear").css('display', 'inline-block');
            }
        }, 400);
    });

    $("#ResetAll").click(resetAll);
    $("#ResetN").click(function() {
        $("#InputNvalue").val("");
    });
});

function setEquation() {
    N_VALUE = 10;
    var tN = $("#InputNvalue").val();
    if (tN != "") {
        if (/^\d+$/.test(tN) && Number(tN) > 0) {
            N_VALUE = Number(tN) - 1;
            ErrorMsgBottom("InputNvalueErrorMsg", null, null, NONVISIBLE_SET);
        } else {
            ErrorMsgBottom("InputNvalueErrorMsg", "Positive Integer", "Please enter a positive integer", VISIBLE_SET);
            return false;
        }
    } else {
        $("#InputNvalue").val(N_VALUE);
        N_VALUE--;
    }
    if (N_VALUE >= 200) {
        $("#InputNvalue").val(200);
        N_VALUE = 199;
    }

    var ins_ust = [],
        ins_usp = [];
    for (var i = 0; i <= N_VALUE; i++)
        ins_ust.push(uStep(i)), ins_usp.push(uSample(i));

    var eqStr = $("#InputEquation").val();
    if (eqStr != "" && eqStr != null) {
        ErrorMsgBottom("InputEqErrorMsg", null, null, NONVISIBLE_SET);

        eqStr = eqStr.toLowerCase().replace(/ /g, '');
        $("#InputEquation").val(eqStr);

        var eqRes = DiffEqParser(eqStr);
        if (eqRes.state == CONST_STATE_FALSE) {
            var errStr = eqRes.val.val;
            var s = eqStr.indexOf(errStr);
            var e = s + errStr.length;
            console.log(s + " " + e);
            highlightText("InputEquation", s, e);
            ErrorMsgBottom("InputEqErrorMsg", "Error", eqRes.val.msg, VISIBLE_SET);
        } else {
            SYS_DIFF_EQUATION = eqRes;
            SYS_UNIT_STEP_RESPONSE = [];
            SYS_UNIT_SAMPLE_RESPONSE = [];
            ErrorMsgBottom("InputEqErrorMsg", null, null, NONVISIBLE_SET);

            seperateTerms(SYS_DIFF_EQUATION);

            ustResponse(SYS_DIFF_EQUATION);
            uspResponse(SYS_DIFF_EQUATION);

            chartLoad(SYSTEM_UST, CONST_UNIT_STEP, ins_ust.addFirst(INS_USTEP_DATA_LABLE), SYS_UNIT_STEP_RESPONSE.addFirst(OUT_RESPONSE_DATA_LABLE));
            chartLoad(SYSTEM_USP, CONST_UNIT_SAMPLE, ins_usp.addFirst(INS_USAMPLE_DATA_LABLE), SYS_UNIT_SAMPLE_RESPONSE.addFirst(OUT_RESPONSE_DATA_LABLE));

            setTimeout(setPoleZero, 300);

            if (OLD_SYS_EQSTR != eqStr) {
                $("#detVergent").text("");
                $("#detConstant").text("");
                $("#detPoles").text("");
                $("#detZeros").text("");

                OLD_SYS_EQSTR = eqStr;
                setTimeout(function() {
                    var uStep_vt = DetVergent(SYS_DIFF_EQUATION, uStep);
                    console.log("=========================");
                    var uSample_vt = DetVergent(SYS_DIFF_EQUATION, uSample);
                    var cStr = uStep_vt.result ? "Unit-Step Response: Convergent.<br>" : "Unit-Step Response: Divergent.<br>";
                    cStr += uSample_vt.result ? "Unit-Sample Response: Convergent.<br>" : "Unit-Sample Response: Divergent.<br>";
                    $("#detVergent").html(cStr);
                    __Debug("DetVergent", { 'ustep': uStep_vt, 'usample': uSample_vt });
                }, 1000);
            }
        }
        __Debug("Btn:SetEquation", SYS_DIFF_EQUATION);
    } else {
        // $("#InputNvalue").val("");
        ErrorMsgBottom("InputEqErrorMsg", "Empty", "Please enter the difference equation!", VISIBLE_SET);
    }
}

function setPoleZero() {
    var constant = seperateTerms(SYS_DIFF_EQUATION);
    var zeros = solvePoly3(POLY_X),
        poles = solvePoly3(POLY_Y),
        zerosStr = "Can't solve.",
        polesStr = "Can't solve.";

    if (zeros) zeros = zeros.r, zerosStr = "";
    else zeros = [];
    if (poles) poles = poles.r, polesStr = "";
    else poles = [];

    for (var i = 0; i < zeros.length; i++) {
        var termStr;
        if (zeros[i].hasOwnProperty('re')) {
            termStr = zeros[i].re.toFixed(3);
            if (zeros[i].im > 0)
                termStr += "+";
            if (zeros[i].im != 0)
                termStr += zeros[i].im.toFixed(3) + "i";
        } else {
            termStr = zeros[i].toFixed(3);
        }
        zerosStr += termStr;
        if (i < zeros.length - 1)
            zerosStr += ", ";
    }

    for (var i = 0; i < poles.length; i++) {
        var termStr;
        if (poles[i].hasOwnProperty('re')) {
            termStr = poles[i].re.toFixed(3);
            if (poles[i].im > 0)
                termStr += "+";
            if (poles[i].im != 0)
                termStr += poles[i].im.toFixed(3) + "i";
        } else {
            termStr = poles[i].toFixed(3);
        }
        polesStr += termStr;
        if (i < poles.length - 1)
            polesStr += ", ";
    }

    $("#detConstant").text("k = " + constant);
    $("#detPoles").text("z = " + polesStr);
    $("#detZeros").text("z = " + zerosStr);
}

function DiffEqParser(str) {
    if (str.indexOf('=') != -1)
        return new Vitem(CONST_STATE_FALSE, { 'val': '=', 'msg': "The difference equation is wrong, please examine." });
    else if (str.indexOf("y[n]") != -1)
        return new Vitem(CONST_STATE_FALSE, { 'val': 'y[n]', 'msg': "The difference equation is wrong, please examine." });
    else if (str.indexOf("y[n-0]") != -1)
        return new Vitem(CONST_STATE_FALSE, { 'val': 'y[n-0]', 'msg': "The difference equation is wrong, please examine." });
    else if (str.indexOf("y[n+0]") != -1)
        return new Vitem(CONST_STATE_FALSE, { 'val': 'y[n+0]', 'msg': "The difference equation is wrong, please examine." });


    var termIdx = [];
    var rightIdx = [];
    var termPosRegex = new RegExp(/([xy])/g);
    var termRes;
    while ((termRes = termPosRegex.exec(str)) !== null) {
        if (termRes.index === termPosRegex.lastIndex)
            termPosRegex.lastIndex++;
        termIdx.push(termRes.index);
        var idx = str.indexOf(']', termRes.index);
        if (idx != -1)
            rightIdx.push(idx);
    }

    if (termIdx.length != rightIdx.length || termIdx.length == 0)
        return new Vitem(CONST_STATE_FALSE, { 'val': str, 'msg': "The difference equation is wrong, please examine." });

    var tn = 0,
        eq = [];
    var coef, coefStr, tpr, delay, signal;
    for (var i = 0; i < str.length && tn < termIdx.length;) {
        // check invalid
        // coefficient must be at left of x or y term
        if (tn < termIdx.length - 1) {
            if (str[rightIdx[tn] + 1] != '+' && str[rightIdx[tn] + 1] != '-') {
                __Debug("DiffEqParser", { 'msg': 'Coefficient is not at left of previous x or y term.', 'term': str.substring(termIdx[tn], rightIdx[tn] + 1) });
                return new Vitem(CONST_STATE_FALSE, { 'val': str.substring(termIdx[tn], rightIdx[tn] + 1), 'msg': "The position of the coefficient must be at the left of x or y term, where " + str.substring(termIdx[tn], rightIdx[tn] + 1) + "." });
            }
        } else {
            if (rightIdx[tn] != str.length - 1) {
                __Debug("DiffEqParser", { 'msg': 'Coefficient is not at left of last x or y term.', 'term': str.substring(termIdx[tn], rightIdx[tn] + 1) });
                return new Vitem(CONST_STATE_FALSE, { 'val': str.substring(termIdx[tn], rightIdx[tn] + 1), 'msg': "The position of the coefficient must be at the left of x or y term, where " + str.substring(termIdx[tn], rightIdx[tn] + 1) + "." });
            }
        }

        coefStr = str.substring(i, termIdx[tn]);
        if (coefStr.length == 1 && (coefStr[0] == '+' || coefStr[0] == '-'))
            coefStr = coefStr + "1";
        else if (tn < 1 && coefStr == "")
            coefStr = 1;
        else if (coefStr.last() == '*')
            coefStr = coefStr.substring(0, coefStr.length - 1);

        if (coefStr.length > 1 && (coefStr.last() == '+' || coefStr.last() == '-' || coefStr.last() == '/')) {
            // The last character of the coefficient must not be '+', '-' and '/'
            __Debug("DiffEqParser", { 'msg': 'Coefficient is wrong at last character.', 'coef': coefStr });
            return new Vitem(CONST_STATE_FALSE, { 'val': coefStr, 'msg': "The coefficien of the term, " + str.substring(termIdx[tn], rightIdx[tn] + 1) + ", is wrong format." });
        }

        // check coefficient
        try {
            coef = math.eval(coefStr);
        } catch (e) {
            __Debug("DiffEqParser", { 'msg': 'Parsing coefficient goes wrong.', 'coef': coefStr });
            return new Vitem(CONST_STATE_FALSE, { 'val': coefStr, 'msg': "The coefficien of the term, " + str.substring(termIdx[tn], rightIdx[tn] + 1) + ", is wrong format." });
        }

        tpr = ParseTerm(str.substring(termIdx[tn], rightIdx[tn] + 1));
        if (tpr.state == CONST_STATE_FALSE) {
            __Debug("DiffEqParser", { 'msg': 'Parsing term is wrong at term, ' + str.substring(termIdx[tn], rightIdx[tn] + 1) + '.', 'term': str.substring(termIdx[tn], rightIdx[tn] + 1) });
            return tpr;
        }

        eq.push(new Term(coef, tpr.val.delay, tpr.val.signal));
        i = rightIdx[tn] + 1;
        tn++;
    }

    // integrate terms
    var neq = []
    var nt;
    for (var i = 0; i < eq.length; i++) {
        nt = eq[i];
        for (var j = i + 1; j < eq.length; j++) {
            if (nt.delay == eq[j].delay && nt.signal == eq[j].signal) {
                nt.coef += eq[j].coef;
                eq.splice(j, 1);
            }
        }
        if (nt.coef != 0)
            neq.push(nt);
    }

    var nx = 0;
    for (var i = 0; i < neq.length; i++)
        if (neq[i].signal == CONST_X)
            nx++;
    if (!nx) return new Vitem(CONST_STATE_FALSE, { 'val': str, 'msg': "The difference equation is no input signal." });

    return neq;
}

function ParseTerm(str) {
    var re = new RegExp(TERM_REGEX);
    var parsed;
    if ((parsed = re.exec(str)) === null)
        return new Vitem(CONST_STATE_FALSE, { 'val': str, 'msg': 'The term, ' + str + ', is wrong format.' });

    var signal = (parsed[1] == 'x') ? CONST_X : CONST_Y;
    var sign = (parsed[2] == '-') ? -1 : 1;
    if (parsed[3] == undefined || parsed[3] == "" || parsed[3] == null)
        parsed[3] = "0";
    var delay = sign * Number(parsed[3]);

    if (delay > 0)
        return new Vitem(CONST_STATE_FALSE, { 'val': str, 'msg': 'The term, ' + str + ', will result in a noncausal system.' });
    return new Vitem(CONST_STATE_TRUE, { 'delay': delay, 'signal': signal });
}

function DetVergent(eq, callback) {
    var t = [];
    var mdel = getMinDelay(eq) * -1 + 3;
    var convergent = 0;
    var divergent = 0;
    var f, yn, result, account = 0,
        accounts = 0;
    var dmdel = mdel * 2 + 7;

    for (var i = 0; i < dmdel; i++)
        t.push(0);

    for (var i = 0; i < dmdel; i++) {
        yn = 0;
        for (var j = 0; j < eq.length; j++) {
            if (eq[j].signal == CONST_X) {
                yn += eq[j].coef * callback(i + eq[j].delay);
            } else {
                if (i + eq[j].delay < 0) yn += 0;
                else yn += eq[j].coef * t[(i + eq[j].delay) % dmdel];
            }
        }
        t[i % dmdel] = yn;
        f = yn;
    }

    for (var i = dmdel;; i++) {
        yn = 0;
        for (var j = 0; j < eq.length; j++) {
            if (eq[j].signal == CONST_X) {
                yn += eq[j].coef * callback(i + eq[j].delay);
            } else {
                if (i + eq[j].delay < 0) yn += 0;
                else yn += eq[j].coef * t[(i + eq[j].delay) % dmdel];
            }
        }
        t[i % dmdel] = yn;

        if (f == Number.POSITIVE_INFINITY || f == Number.NEGATIVE_INFINITY || f == Number.INFINITY) {
            result = 0;
            break;
        }

        if (i > 1e6) {
            var diff = Math.abs(t[(i - 1) % dmdel] - t[i % dmdel]);
            if (diff <= 1e-5) {
                account++;
            } else {
                account--;
            }
            accounts++;
            if (accounts > dmdel) {
                break;
            }

        }
        f = yn;
    }

    if (account == accounts) result = 1;
    else result = 0;

    if (result) {
        if (f == Number.POSITIVE_INFINITY || f == Number.NEGATIVE_INFINITY || f == Number.INFINITY)
            return { 'result': 0, 'val': f };
        return { 'result': 1, 'val': f };
    } else {
        return { 'result': 0, 'val': f };
    }
}

function ustResponse(eq) {
    for (var i = 0; i <= N_VALUE; i++)
        SYS_UNIT_STEP_RESPONSE.push(0);

    var yn = 0;
    for (var i = 0; i <= N_VALUE; i++) {
        yn = 0;
        for (var j = 0; j < eq.length; j++) {
            if (eq[j].signal == CONST_X) {
                yn += eq[j].coef * uStep(i + eq[j].delay);
            } else {
                if (i + eq[j].delay < 0)
                    yn += 0;
                else
                    yn += eq[j].coef * SYS_UNIT_STEP_RESPONSE[i + eq[j].delay];
            }
        }
        SYS_UNIT_STEP_RESPONSE[i] = yn;
    }
    __Debug("ustResponse:SYS_UNIT_STEP_RESPONSE", SYS_UNIT_STEP_RESPONSE);
}

function uspResponse(eq) {
    for (var i = 0; i <= N_VALUE; i++)
        SYS_UNIT_SAMPLE_RESPONSE.push(0);

    var yn = 0;
    for (var i = 0; i <= N_VALUE; i++) {
        yn = 0;
        for (var j = 0; j < eq.length; j++) {
            if (eq[j].signal == CONST_X) {
                yn += eq[j].coef * uSample(i + eq[j].delay);
            } else {
                if (i + eq[j].delay < 0)
                    yn += 0;
                else
                    yn += eq[j].coef * SYS_UNIT_SAMPLE_RESPONSE[i + eq[j].delay];
            }
        }
        SYS_UNIT_SAMPLE_RESPONSE[i] = yn;
    }
    __Debug("uspResponse:SYS_UNIT_SAMPLE_RESPONSE", SYS_UNIT_SAMPLE_RESPONSE);
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

function chartLoad(sys, type, inData, outData) {
    var uld = (type == CONST_UNIT_STEP) ? INS_USTEP_DATA_LABLE : INS_USAMPLE_DATA_LABLE;
    var cls;
    if (type == CONST_UNIT_STEP) {
        uld = INS_USTEP_DATA_LABLE;
        cls = {
            'Unit-Step': '#727272',
            'Response': '#388E3C'
        };
    } else {
        uld = INS_USAMPLE_DATA_LABLE;
        cls = {
            'Unit-Sample': '#727272',
            'Response': '#388E3C'
        };
    }

    sys.load({
        'columns': [inData, outData],
        'type': 'scatter',
        'unload': [uld, OUT_RESPONSE_DATA_LABLE]
    });
    sys.data.colors(cls);
}

function resetAll() {
    ErrorMsgBottom("InputNvalueErrorMsg", null, null, NONVISIBLE_SET);
    ErrorMsgBottom("InputEqErrorMsg", null, null, NONVISIBLE_SET);

    if (!SYS_UNIT_STEP_RESPONSE.length && $("#InputNvalue").val() == "" && $("#InputEquation").val("") == "")
        return false;

    N_VALUE = 10;
    SYS_UNIT_STEP_RESPONSE = [];
    SYS_UNIT_SAMPLE_RESPONSE = [];
    $("#InputNvalue").val("");
    $("#InputEquation").val("");
    $("#detVergent").text("");
    $("#detConstant").text("");
    $("#detPoles").text("");
    $("#detZeros").text("");
    OLD_SYS_EQSTR = "";

    if (SYSTEM_UST) {
        SYSTEM_UST.unload({
            'ids': [INS_USTEP_DATA_LABLE, OUT_RESPONSE_DATA_LABLE]
        });
    }
    if (SYSTEM_USP) {
        SYSTEM_USP.unload({
            'ids': [INS_USAMPLE_DATA_LABLE, OUT_RESPONSE_DATA_LABLE]
        });
    }
}

function highlightText(id, s, e) {
    var input = document.getElementById(id);
    input.setSelectionRange(s, e); // Highlights "Cup"
    input.focus();
}

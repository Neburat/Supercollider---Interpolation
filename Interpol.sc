//Interpol 2 -Ft Darien

Interpol {
	var <>r,<>interp, >inA,>inB,>time, <>callPrint;
	var <>resolution,<>timeStep,<>curve,<>x,<>storeTime,<>storeInpB;

	*new{ | inpA,time, inCurve |
		^super.new.init( inpA, time, inCurve);
	}

	init{ | inpA,time, inCurve |

		inA = inB = interp = inpA;
		x = 1;
		if(time.isNil) {time = 10};  //def time  10 sec
		storeTime = time;
		curve = inCurve ? "lin";     //def curve
		resolution = time*50;
		timeStep = time*0.001;
		callPrint = false;
	}

	//Print result
	call { | inpB, time, inCurve |
		var mapLo;


		x = 1;
		storeInpB = inpB;
		if (inCurve.notNil) { curve = inCurve };
		if (inpB.notNil) { inB = inpB; inA = interp; };
		if (time.notNil) {
			resolution = time*50;
			timeStep = time*0.001;
			storeTime = time;
		};



		case
		{curve == "lin"}  {

			mapLo = 0 ;
			r = Routine {
				var i = 0, a = 0;
				inf.do{
					while({ i < resolution },{
						if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
						timeStep.wait;
						interp = blend(inA,inB,a.linlin(mapLo,resolution,mapLo,1.0));
						if (callPrint) { this.printCalls };
					});
					"done".postln;
					r.stop
				}
			}.play
		}

		{curve == "exp"} {

			mapLo = 0.0001 ;
			r = Routine {
				var i = 0, a = 0;
				inf.do{
					while({ i < resolution },{
						if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
						timeStep.wait;
						interp = blend(inA,inB,a.expexp(mapLo,resolution,mapLo,1.0));
						if (callPrint) { this.printCalls };
					});
					"done".postln;
					r.stop
				}
			}.play
		}
	}

	printCalls {
		interp.postln;
	}

	///stop, restart & getState
	callStop{
		x = 0;
		"currentState".postln;
		r.stop;
		^interp;
	}

	callGo{
		this.callStop;
		this.call(storeInpB, storeTime);
	}

	getState{
		^interp;
	}

}
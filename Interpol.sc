// ---- An even more economic version ---
// Added a mechanism for being able to call several times without overlapping Routines

Interpol {
	var <>r,<>interp, >inA,>inB,>time, <>callPrint;
	var <>resolution,<>timeStep,<>curve,<>x,<>storeTime,<>storeInpB, <>isRunning;

	*new{ | inpA |
		^super.new.init( inpA );
	}

	init { | inpA |

		inA = inB = interp = inpA;
		x = 1;
		if (time.isNil) { time = 10};
		storeTime = 10;
		curve = "lin";
		resolution = time * 50;
		timeStep = time * 0.001;
		callPrint = false;
		isRunning = false; //Check if it's running
	}


	call { |inpB, time, inCurve|
		if (isRunning.not) {
			this.run(inpB, time, inCurve);
		} { this.callStop; r.stop; r.free; this.run(inpB, time, inCurve) }
	}


	//Print result
	run { | inpB, time, inCurve |
		var mapLo;
		isRunning = true;
		//If it was playing, stop it and start a new one from where it left

		x = 1;
		storeInpB = inpB;

		if (inCurve.notNil) { curve = inCurve };
		if (inpB.notNil) { inA = interp; inB = inpB };
		if (time.notNil) {
			resolution = time * 50;
			timeStep = time * 0.001;
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
					r.stop;
					isRunning = false;
				}
			}.play
		}

		{curve == "exp"} {
			mapLo = 0.0001 ;
			r = Routine {
				var i = 0, a = 0;
				inf.do{
					while( { i < resolution },{
						if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
						timeStep.wait;
						interp = blend(inA,inB,a.expexp(mapLo,resolution,mapLo,1.0));
						if (callPrint) { this.printCalls };
					});
					"done".postln;
					r.stop;
					isRunning = false;
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
		isRunning = false;
		"currentState".postln;
		^interp;
	}

	callGo{
		this.run(storeInpB, storeTime);
	}

	getState{
		"currentState".postln;
		^interp;
	}

}
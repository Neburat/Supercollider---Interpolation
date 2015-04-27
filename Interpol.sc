// ---- A tiddier version with additional mapping curves --- [ April 27, 2015 ]

// Added different interpolation types
// Added a method to query interpolation types

Interpol2 {
	var <>r,<>interp, >inA,>inB,>time, <>callPrint;
	var <>resolution,<>timeStep,<>curve,<>x,<>storeTime,<>storeInpB;

	*new{ | inpA |
		^super.new.init( inpA );
	}

	init { | inpA |

		inA = inB = interp = inpA;
		x = 1;
		if (time.isNil) { time = 10 };
		storeTime = 10;
		curve = \lin;
		resolution = time * 50;
		timeStep = time * 0.001;
		callPrint = false;
	}

	//Print result (add a value for bending)
	call { | inpB, time, inCurve, bend = 4 |
		var mapLo;
		//If it was playing, stop it and start a new one from where it left
		r.stop;
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
		{curve == \lin}        {this.mapLin}
		{curve == \exp}        {this.mapExp}
		{curve == \linexp}     {this.mapLinExp}
		{curve == \explin}     {this.mapExpLin}
		//Has a random "center" value
		{curve == \bilin}      {this.mapBiLinCurve}
		{curve == \biexp}      {this.mapBiExpCurve}
		//Take an angle ("bend") argument
		{curve == \lincurve}   {this.mapLinCurve(bend)}
		{curve == \curvelin}   {this.mapCurveLin(bend)}
		{curve == \lCurve}     {this.mapLCurve(bend)}
		{curve == \gaussCurve} {this.mapGaussCurve(bend)}
		//Behaves as an easeIn/easeOut kind of interpolation
		{curve == \sCurve}     {this.mapScurve};
	}

	what {
		var tags = [\lin, \exp, \linexp,\explin,\bilin,\biexp,\lincurve,\curvelin,\lCurve,\gaussCurve,\sCurve];
		"Curve types are: ".postln;
		"".postln;
		tags.do({|tag| ($\t ++ " " ++ $\\++tag ++ $\t).postln });
		"".postln;
	}

		printCalls {
		interp.postln;
	}

	///stop, restart & getState
	callStop{
		x = 0;
		"currentState".postln;
		^interp;
	}

	callGo{
		this.call(storeInpB, storeTime);
	}

	getState{
		"currentState".postln;
		^interp;
	}

	// --------------[ Interpolation types ]----------------- //

	mapLin {
		var mapLo = 0.0 ;
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
			}
		}.play
	}

	mapExp {
		var mapLo = 0.0001 ;
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
			}
		}.play
	}

	mapLinExp {
		var mapLo = 0.0001 ;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.linexp(mapLo,resolution,mapLo,1.0));
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}


	mapExpLin {
		var mapLo = 0.0001;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.explin(mapLo,resolution,mapLo,1.0));
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}

	mapLinCurve { | bend |
		var	mapLo = 0.0 ;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.lincurve(mapLo,resolution,mapLo,1.0, bend));
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;

			}
		}.play
	}

	mapCurveLin { | bend |
		var mapLo = 0.0;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.curvelin(mapLo,resolution,mapLo,1.0, bend));
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;

			}
		}.play
	}

	mapBiLinCurve {
		var mapLo = 0.0 ;
		var midA = resolution.rand;
		var midB = 1.0.rand;

		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.bilin(
						midA, mapLo, resolution, midB, mapLo, 1.0) // uses random centers
					);
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}

	mapBiExpCurve {
		var mapLo = 0.0001;
		var midA = resolution.rand;
		var midB = 1.0.rand;

		//In the remote case that one of them is 0
		if (midA == 0.0) {midA = mapLo};
		if (midB == 0.0) {midB = mapLo};

		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.biexp(
						midA, mapLo, resolution, midB, mapLo, 1.0) // uses random centers
					);
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}

	mapLCurve { | bend |
		var mapLo = 0.0;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.linlin(mapLo,resolution, bend * -1, bend).lcurve());
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}

	mapGaussCurve { | bend |
		var mapLo = 0.0;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					interp = blend(inA,inB,a.linlin(mapLo,resolution, bend * -1, bend).gaussCurve());
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}

	mapScurve {
		var mapLo = 0.0;
		r = Routine {
			var i = 0, a = 0;
			inf.do{
				while( { i < resolution },{
					if(x == 1) {i = i + 1;  a = a + 1} { i = resolution};
					timeStep.wait;
					//.scurve -> input needs to be normalized (unipolar)
					interp = blend(inA,inB,a.linlin(mapLo,resolution, mapLo, 1.0).scurve());
					if (callPrint) { this.printCalls };
				});
				"done".postln;
				r.stop;
			}
		}.play
	}



}
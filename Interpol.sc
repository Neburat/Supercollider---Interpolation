///input1, time, curve

Interpol{
	var <>r,<>interp,>inA,>inB,>time,<>resolution,<>timeStep,<>curve,<>x,<>storeTime,<>storeInpB;

	*new{arg inpA,time, inCurve;
		^super.new.init(inpA,time, inCurve);
	}

	init{arg inpA,time, inCurve;
		this.inA = inpA;
		this.inB = inpA;
		this.x = 1;
		this.storeTime = time;

		if (
			inCurve.notNil ,{this.curve = inCurve;} ,{this.curve = "lin";}
		);


		this.resolution = time*100.0;
		this.timeStep = time*0.001;
		this.interp = inpA;
	}



	////Interpolation


	call{arg inpB, time, inCurve;

		this.x = 1;
		this.storeInpB = inpB;


		if (
			inCurve.notNil ,{this.curve = inCurve}
		);

		if (
			inpB.notNil ,{this.inA = this.interp; this.inB = inpB}
		);

		if (
			time.notNil ,{this.resolution = time*100.0;this.timeStep = time*0.001},{time = this.storeTime;this.resolution = time*100.0;this.timeStep = time*0.001}
		);

		if (this.curve == "lin")  {
			this.r = Routine{

				var i = 0, a = 0;


				{while({i<this.resolution},{
					if( this.x == 1) {i = i + 1;  a = a + 1}
					{i = this.resolution};


					this.timeStep.wait;

					this.interp = blend(inA,inB,a.linlin(0,this.resolution,0,1.0));
					});
					"done".postln;
					this.r.stop
				}.loop



			}.play
		};

		if (this.curve == "exp")  	{
			this.r = Routine{

				var i = 0, a = 0;


				{while({i<this.resolution},{
					if( this.x == 1) {i = i + 1;  a = a + 1}
					{i = this.resolution};


					this.timeStep.wait;

					this.interp = blend(inA,inB,a.expexp(0.0001,this.resolution,0.0001,1.0));
					});
					"done".postln;
					this.r.stop
				}.loop



			}.play
		};



	}



	callPrint{arg inpB, time, inCurve;

		this.x = 1;
		this.storeInpB = inpB;


		if (
			inCurve.notNil ,{this.curve = inCurve}
		);

		if (
			inpB.notNil ,{this.inA = this.interp; this.inB = inpB}
		);

		if (
			time.notNil ,{this.resolution = time*100.0;this.timeStep = time*0.001; this.storeTime = time}
		);

		if (this.curve == "lin")  {
			this.r = Routine{

				var i = 0, a = 0;


				{while({i<this.resolution},{
					if( this.x == 1) {i = i + 1;  a = a + 1}
					{i = this.resolution};


					this.timeStep.wait;

					this.interp = blend(inA,inB,a.linlin(0,this.resolution,0,1.0));
					this.interp.postln;
					});
					"done".postln;
					this.r.stop
				}.loop



			}.play
		};

		if (this.curve == "exp")  	{
			this.r = Routine{

				var i = 0, a = 0;


				{while({i<this.resolution},{
					if( this.x == 1) {i = i + 1;  a = a + 1}
					{i = this.resolution};


					this.timeStep.wait;

					this.interp = blend(inA,inB,a.expexp(0.0001,this.resolution,0.0001,1.0));
					this.interp.postln;
					});
					"done".postln;
					this.r.stop
				}.loop



			}.play
		};



	}



	///stop, restart & getState



	callStop{
		this.x = 0;
		"currentState".postln;
		^this.interp;

	}


	callGo{

		this.call(this.storeInpB, this.storeTime);

	}

	callGoPrint{

		this.callPrint(this.storeInpB, this.storeTime);

	}

	getState{

		"currentState".postln;
		^this.interp;


	}

}

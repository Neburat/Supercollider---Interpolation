# Supercollider---Interpolation


//StartingArray,Time,Line(lin,exp)

h = Interpol.new([1,10],10,"lin")


//Start the interpolation(EndingArray,Time,Line)

h.call([100,9000])


//Print the interpolation

h.callPrint([100,9000])


//Pause the interpolation & GetCurrentState

h.callStop


//Resume the interpolation

h.callGo


//Resume the Print interpolation

h.callGoPrint


//Print current Array

h.getState




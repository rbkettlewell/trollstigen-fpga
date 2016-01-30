/*
	This file provides an instructional example of how to program
	a finite state machine with the Verilog HDL which recognizes
	an even number of ones it has encountered since last reset.
	
	It is provided for educational use courtesty of the University
	of Washington's Department of Computer Science & Engineering.
*/

/*
	You can save several lines of input, output, and reg declarations by
	doing them all in the module's definition. In the event you have buses
	as inputs and outputs simply put the [x:0] immediately before the
	variable's name.
*/
module evenOddChecker(clock, RESET, in, isEven);

// These variables will be used to track what state the machine is currently
// in and what state it will transition to on the next edge of the clock.
// 
// REMEMBER: To represent n states, you will need log2(n) bit wide reg variables!
input clock;
input RESET;
input in;
output isEven;
reg isEven;
reg state, nextState;

// While some may choose to refer to their states as 0 through n-1 (where n = # states),
// the use of parameters allows you easily identify your states and vastly improves
// the readability of your code. 

parameter EVEN = 0;
parameter ODD = 1;

// An initial block allows you to specify the values of your input, output, and internal
// variables when the machine is initialized. In this case, we want to make sure our
// FSM begins in its specified start state.

initial begin
	state = EVEN;
end


// This always block resets the FSM in the event that RESET is asserted or
// updates the current state to the determined next state on each positive edge
// of the clock.
// Note The use of the non-blocking (<=) assignment operator.

always @ (posedge clock) begin
	if(RESET) begin
		state <= EVEN;
	end
	else begin
		state <= nextState;
	end
end

// This always block assigns the FSM's output(s) based on the machine's current state
// and computes what state the machine will transition to next based on the current input values and state.
// The "always @ (*)" is a conveinent shorthand when writing always blocks which are used
// for doing combinational logic. The '*' is replaced by the compiler with the sensitivity list.
// Note that the '=' assignment operator is used, not the '<=' operator in this always block.

always @ (*) begin

	// Since the outputs of the cases only depend on which state the FSM is in, this code is
	// an example of a Moore-style FSM. To implement a Mealy-style FSM, inside the conditional
	// statements determining nextState assign your output(s).
	
	case(state)
	
		EVEN: begin
			isEven = 1;
			
			// While it seems that the else statement below and the default case are not necessary, 
			// remember that you are programming in a Hardware Description Language. Therefore, if 
			// you do not explicitly specify all the branches of an if-else statement you run the risk 
			// of having the synthesizer infer latches (a memory primitive) for nextState (and your output(s) 
			// ifyou are coding a Mealy-style FSM). This can lead to nightmarish debugging! 
			// A few more lines of code can save you many hours in the lab.
			
			if(in)
				nextState = ODD;
			else
				nextState = EVEN;
		end
		ODD: begin
			isEven = 0;
			
			if(in)
				nextState = EVEN;
			else
				nextState = ODD;
		end
		default: begin
			isEven = 1'bx;
			nextState = 1'bx;
		end
		
	endcase
	
end

endmodule

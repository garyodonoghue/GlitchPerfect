var inputs = [
	"userInput1",
	"userInput2",
	"userInput3",
	"userInput4",
	"userInput5"
];

var numbers = [
	1,
	2,
	3,
	4,
	5
];

var userSequence = [];

var startTime = 0;

function assignNumbersToButtons(){
	userSequence = [];
	startTime = new Date().getTime();
	tempInputs = JSON.parse(JSON.stringify(inputs));
	tempNumbers = JSON.parse(JSON.stringify(numbers));
	for(var loopingNumbersIndex = tempInputs.length; loopingNumbersIndex > 0; loopingNumbersIndex--){
		var indexInInputArray = getRandomInput(loopingNumbersIndex);
		var indexInNumbersArray = getRandomInput(loopingNumbersIndex);
		var inputNameForSequence = removeAndReturnElementFromArray(tempInputs, indexInInputArray);
		var numberForSequence = removeAndReturnElementFromArray(tempNumbers, indexInNumbersArray);
		var newNumber = assignNumberToButton(inputNameForSequence, numberForSequence);
	}
}
function assignNumberToButton(input, number){
	$("#"+input).attr("value",""+number).html(number);
	$("#"+input+"Parent").removeClass("green");
	$("#"+input).button().button("enable").button("refresh");
}

function getRandomInput(number){
	return Math.floor((Math.random() * number));
}

function removeAndReturnElementFromArray(array, index){
	return array.splice(index, 1);
}

function isCorrectButtonAndRemoveFromArray(id){
	var number = $("#"+id).attr("value");
	if((userSequence.length + 1) == number){
		$("#"+id).button("disable").button("refresh");
		$("#"+id+"Parent").addClass("green");
		userSequence.push(id);
		if(5 == userSequence.length){
			//Finish sequence
			sendRating((new Date().getTime() - startTime));
		}
	}else{
		//Do failure animation here
		for(var index = userSequence.length; index > 0; index--){
			$("#"+userSequence[index-1]).button("enable");
			$("#"+userSequence[index-1]+"Parent").removeClass("green");
		}
		userSequence = [];
	}
}

function sendRating(timeInMilliSeconds){
	rating = getRating(timeInMilliSeconds);
	passEvent(rating);
}

function getRating(timeInMilliSeconds){
	var starRating = 0;
	var seconds = 3000;
	if(timeInMilliSeconds < (seconds*0.40)){
		starRating = 5;
	}else if(timeInMilliSeconds < (seconds*0.60)){
		starRating = 4;
	}else if(timeInMilliSeconds < (seconds*0.75)){
		starRating = 3;
	}else if(timeInMilliSeconds < (seconds*0.90)){
		starRating = 2;
	}else if(timeInMilliSeconds < seconds){
		starRating = 1;
	}
	return starRating;
}

$(document).ready(function() {
	assignNumbersToButtons();
});
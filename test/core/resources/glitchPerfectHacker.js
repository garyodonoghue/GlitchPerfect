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
	startTime = new Date().getTime();
	tempInputs = inputs;
	tempNumbers = numbers;
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
		$("#"+id).addClass("green").button("disable");
		userSequence.push(id);
		if(5 == userSequence.length){
			//Finish sequence
			sendRating((new Date().getTime() - startTime));
		}
	}else{
		//Do failure animation here
		for(var index = userSequence.length; index > 0; index--){
			$("#"+userSequence[index-1]).button("enable").removeClass("green");
		}
		userSequence = [];
	}
}

function sendRating(timeInMilliSeconds){
	rating = getRating(timeInMilliSeconds);
	console.log(rating);
}

function getRating(timeInMilliSeconds){
	var starRating = 0;
	if(timeInMilliSeconds < 1000){
		starRating = 5;
	}else if(timeInMilliSeconds < 1400){
		starRating = 4;
	}else if(timeInMilliSeconds < 1800){
		starRating = 3;
	}else if(timeInMilliSeconds < 2200){
		starRating = 2;
	}else if(timeInMilliSeconds < 2600){
		starRating = 1;
	}
	return starRating;
}

$(document).ready(function() {
	assignNumbersToButtons();
});
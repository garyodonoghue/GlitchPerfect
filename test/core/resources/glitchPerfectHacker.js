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

var dotsTimer = setInterval(function(){
	dotsTimerFunction();
},700);

var dots = 0;

var userRatingVar = 0;
var userRatingTimer = undefined;

var zeroUserRatingString = "System.out.println('FAIL');";
var oneUserRatingString = "gg";
var twoUserRatingString = "\"I'm sorry. I'm afraid I can't do that.\"";
var threeUserRatingString = "int hacks = 1;";
var fourUserRatingString = "bool WIN = true;";
var fiveUserRatingString = "bool theCakeIsALie = false;";

$.fn.stars = function() {
    return $(this).each(function() {
        // Get the value
        var val = parseFloat($(this).html());
        // Make sure that the value is in 0 - 5 range, multiply to get width
        var size = Math.max(0, (Math.min(5, val))) * 16;
        // Create stars holder
        var $span = $('<span />').width(size);
        // Replace the numerical value with stars
        $(this).html($span);
    });
}

function assignNumbersToButtons(){
	userSequence = [];
	tempInputs = JSON.parse(JSON.stringify(inputs));
	tempNumbers = JSON.parse(JSON.stringify(numbers));
	for(var loopingNumbersIndex = tempInputs.length; loopingNumbersIndex > 0; loopingNumbersIndex--){
		var indexInInputArray = getRandomInput(loopingNumbersIndex);
		var indexInNumbersArray = getRandomInput(loopingNumbersIndex);
		var inputNameForSequence = removeAndReturnElementFromArray(tempInputs, indexInInputArray);
		var numberForSequence = removeAndReturnElementFromArray(tempNumbers, indexInNumbersArray);
		var newNumber = assignNumberToButton(inputNameForSequence, numberForSequence);
	}
	startTime = new Date().getTime();
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
	$("#rating").html(rating);
	$('span.stars').stars();
	$("#timeTaken").html(timeInMilliSeconds/1000);
	setUserRatingTimeout(rating);
	passEvent(rating);
}

function setUserRatingTimeout(){
	userRatingVar = 0;
	clearInterval(userRatingTimer);
	if(rating == 0){
		userRatingString = zeroUserRatingString;
	}else if(rating == 1){
		userRatingString = oneUserRatingString;
	}else if(rating == 2){
		userRatingString = twoUserRatingString;
	}else if(rating == 3){
		userRatingString = threeUserRatingString;
	}else if(rating == 4){
		userRatingString = fourUserRatingString;
	}else if(rating == 5){
		userRatingString = fiveUserRatingString;
	}
	userRatingTimer = setInterval(function(){
		userRatingTimerFunction();
	},70);
}

function userRatingTimerFunction(){
	if(userRatingVar > userRatingString.length){
		userRatingVar = 0;
		clearInterval(userRatingTimer);
	}else{
		$("#userRating").html(userRatingString.substring(0, userRatingVar));
		userRatingVar++;
	}
}

function getRating(timeInMilliSeconds){
	var starRating = 0;
	var seconds = 3000;
	if(timeInMilliSeconds < (seconds*0.50)){
		starRating = 5;
	}else if(timeInMilliSeconds < (seconds*0.62)){
		starRating = 4;
	}else if(timeInMilliSeconds < (seconds*0.77)){
		starRating = 3;
	}else if(timeInMilliSeconds < (seconds*0.90)){
		starRating = 2;
	}else if(timeInMilliSeconds < seconds){
		starRating = 1;
	}
	return starRating;
}

function dotsTimerFunction() {
	var dotString = "";
	if(dots < 4){
		for(var i = dots; i > 0; i--){
			dotString = dotString + ".";
		}
	}else{
		dots = 0;
	}
	$("#addDots").html(dotString);
	$("#addDots2").html(dotString);
	dots++;
}

$(document).ready(function() {
	$('span.stars').stars();
	assignNumbersToButtons();
});
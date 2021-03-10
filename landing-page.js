window.addEventListener('DOMContentLoaded', () => {
document.getElementById("to-how-to").addEventListener('click', toHowTo);
document.getElementById("to-set-up").addEventListener('click', toSetUp);
document.getElementById("download-wepomo").addEventListener('click', osSupport);
setInterval(funFacts, 5000);
});

function toHowTo(){
    window.location.href = "./how-to-page.html";
}

function toSetUp(){
    window.location.href = "./setup-active-break-pages.html";
}
function osSupport(){
    document.getElementById("download-wepomo").href = "../wepomo-win32-x64.zip";
    if (navigator.appVersion.indexOf("Win") != -1){
        document.getElementById("download-wepomo").href = "../wepomo-win32-x64.zip";
    } 
    if (navigator.appVersion.indexOf("Mac") != -1) {
        document.getElementById("download-wepomo").href = "../wepomo-darwin-x64.zip";
    }
    if (navigator.appVersion.indexOf("Linux") != -1){
        document.getElementById("download-wepomo").href = "../wepomo-linux-x64.zip";
    }
}


let facts = ['Fun Fact: Pomodoro means Tomato in Italian!',
            'Fun Fact: Francesco Cirillo was the original creator of the Pomodoro Timer!', 
            'Fun Fact: The Pomodoro timer was inspiried by a tomato shaped timer!', 
            "Remember: The Pomodoro timer's biggest strength is its simplicity!"];

let num = 0;

function funFacts(){
    if (num >= facts.length) num = 0;
    document.getElementById('fun-facts').innerHTML = facts[num];
    num++;
}

exports.toHowTo = toHowTo;
exports.toSetUp = toSetUp;
exports.osSupport = osSupport;
exports.funFacts = funFacts;

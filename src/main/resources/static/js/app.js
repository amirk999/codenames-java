var stompClient = null;
var gameId = null;

$('#find-btn').click(initGame);

// function(){
//     $.ajax({
//         url: '/games/find',
//         type: 'POST',
//         dataType: 'json',
//         contentType: 'application/json; charset=utf-8',
//         data: JSON.stringify(getFormData($('#game-form'))),
//         success: openGame,
//         error: function (xhr, resp, text) {
//             console.log(xhr, resp, text);
//         }
//     });
// });

$('#create-btn').click(initGame);

$('.card').on('click', tapCard);

function tapCard(e) {
    var index = e.delegateTarget.getAttribute('index');
    console.log(index);
    stompClient.send('/app/play/'+gameId, {}, JSON.stringify({'card': index, 'game': gameId}));
}

function openGame(data) {
    // console.log(data);
    gameId = data.id;
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function connect() {
        console.log(gameId);
        stompClient.subscribe('/app/game/join/'+gameId, onMessage);
        stompClient.subscribe('/game/status/'+gameId, onMessage);
    });
}

function onMessage(message) {
    // console.log(JSON.parse(message.body));
    $.each(JSON.parse(message.body).cards, function(index, item) {
        $(`#index-${item.index}`).attr({class: `card status-${item.status} color-${item.color}`});
        $(`#card-${item.index}`).html(item.body);
        // console.log(item);
    });
}


function initGame(e) {
    var action = '/games';
    if(e.target.id == 'find-btn') {
        action += '/find';
    }
    $.ajax({
        url: action,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(getFormData($('#game-form'))),
        success: openGame,
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}
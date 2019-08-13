const setColorInputLabel = (name, color) => {
    let resultString = 'input' + name.charAt(0).toUpperCase() + name.slice(1) + 'Label';
    document.getElementById(resultString).style.color = color;
};

const addValidationMessages = (key, messages) => {
    let resultString = key + 'Messages';
    let element = document.getElementById(resultString);
    element.innerText = messages.join('\n');
    element.style.color = '#d93b3b';
    element.hidden = false;
};

const setListenerForInput = (name, scope) => {
    let resultString = 'exampleInput' +  name.charAt(0).toUpperCase() + name.slice(1);
    let element = document.getElementById(resultString);
    element.addEventListener('input', () => {
        setColorInputLabel(name, 'black');
        scope.message = '';
    })
};

const hideValidationMessages = () => {
    [].forEach.call(document.getElementsByClassName('validationMessages'), function (el) {
        console.log(el.id);
        el.hidden = true;
    });
};

angular.module("registration_form",[])
    .controller("AppCtrl", function ($scope, $http) {
        $scope.auth = {};

        let resultMessageEl = document.getElementById('resultMessage');


        $scope.sendForm = function(auth){
            console.log($scope.auth);

            Object.keys($scope.auth)
                .forEach(key => setListenerForInput(key, $scope));

            console.log(JSON.stringify($scope.auth));
            $http({
                method: "POST",
                url: "app/api/users",
                data: JSON.stringify($scope.auth),
                headers: { "Content-Type" : "application/json" }
            }).then(
                (data) => {
                    console.log(data);
                    hideValidationMessages();
                    Object.keys($scope.auth)
                        .forEach(key => {
                            setColorInputLabel(key, 'black');
                            $scope.auth[key] = '';
                        });
                resultMessageEl.className = 'alert alert-success';
                resultMessageEl.innerText = data.data;
                resultMessageEl.hidden = false;
        },
            (error) => {
                console.log(error);
                hideValidationMessages();
                let note = error.data;

                Object.keys(note.validationMessages)
                    .forEach(key => {
                        console.log(key);
                        addValidationMessages(key, note.validationMessages[key]);
                    });

                resultMessageEl.className = 'alert alert-warning';
                resultMessageEl.innerText = note.localizedMessage;
                resultMessageEl.hidden = false;

                delete note.validationMessages;
                delete note.status;
                delete note.localizedMessage;

                Object.keys(note)
                    .forEach(
                        key => {
                            $scope.auth[key] = note[key];
                            if (note[key] === '') setColorInputLabel(key, '#d93b3b');
                        });

            }
        );
        }
    });
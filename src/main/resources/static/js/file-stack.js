function pickFile(){
    let client = filestack.init(FILESTACK_API);
    client.pick({
        accept: 'image/*',
        maxFiles: 1,
    })
        .then(function (result) {
            console.log(JSON.stringify(result.filesUploaded[0].url));
        });

    window.onload = function() {
        let btn = document.getElementById("filePicker");
        btn.onclick = pickFile;
    }

}
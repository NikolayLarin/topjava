let context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    /*
        $(".delete").click(function () {
            if (confirm('Are you sure?')) {
                deleteRow($(this).attr("id"));
            }
        });
    */

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    if (confirm("Are you sure?")) {
        $.ajax({
            url: context.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            updateTable();
            successNoty("Deleted");
        });
    }
}

function updateTable() {
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        successNoty("Saved");

        switch (context.ajaxUrl) {
            case "ajax/profile/meals/":
                drawFiltered();
                break;
            case "ajax/admin/users/":
                updateTable();
                break;
        }
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomLeft",
        timeout: 1500
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomLeft",
        timeout: 1500
    }).show();
}
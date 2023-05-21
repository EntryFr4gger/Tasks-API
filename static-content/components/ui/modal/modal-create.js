import {button, div, form, h1, h5, i, input, label, svg, textarea, use} from "../../dom/domTags.js";

export function ModalCreate(func, id) {
    return div(
        div(
            {class:"icon-card-create"},
            i({class: "fas fa-plus", style: {color: "#000000"}}),
            button({type: "button", class: "btn", "data-bs-toggle": "modal", "data-bs-target": "#exampleModal"},
                "Add Card",
            )
        ),

        div({
                class: "modal fade",
                id: "exampleModal",
                tabindex: "-1",
                "aria-labelledby": "exampleModalLabel",
                "aria-hidden": "true"
            },
            div({class: "modal-dialog"},
                div({class: "modal-content"},
                    div({class: "modal-header"},
                        h5({class: "modal-title", id: "exampleModalLabel"}, "Create Card"),
                        button({type: "button", class: "btn-close", "data-bs-dismiss": "modal", "aria-label": "Close"})
                    ),
                    form(
                        {onSubmit: func},
                        div({class: "modal-body"},

                            div({class: "mb-3"},
                                label({for: "name", class: "form-label"}, "Name"),
                                input({type: "text", class: "form-control", id: `name-card`})
                            ),
                            div({class: "mb-3"},
                                label({for: "message", class: "form-label"}, "Description"),
                                textarea({class: "form-control", id: "description-card"})
                            )
                        ),
                        div({class: "modal-footer"},
                            button({type: "button", class: "btn btn-secondary", "data-bs-dismiss": "modal"}, "Close"),
                            input({type: "submit", class: "btn btn-primary", "data-bs-dismiss": "modal"}, "Submit")
                        )
                    )
                )
            )
        )
    )

}
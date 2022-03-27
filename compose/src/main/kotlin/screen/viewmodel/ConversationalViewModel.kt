package screen.viewmodel

import usecase.ConversationalUseCase

class ConversationalViewModel(
    private val conversationalUseCase: ConversationalUseCase
) {

    fun processData(){
        //conversationalUseCase.processValues()
    }
}
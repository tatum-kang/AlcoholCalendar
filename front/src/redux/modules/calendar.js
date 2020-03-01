import { createAction, handleActions } from 'redux-actions';
import moment from 'moment';
import produce from "immer"



const DATE_CHANGE = 'calendar/DATE_CHANGE';
export const changeDate = createAction(DATE_CHANGE);



const initialState = {
  date: moment(),
}


export default handleActions({
  [DATE_CHANGE]: (state, action) => {
    return produce(state, draft => {
      draft.date = action.payload
    })
  }
}, initialState)
import { combineReducers } from 'redux';
import base from './base';
import auth from './auth';
import user from './user';
import calendar from './calendar'
import { penderReducer } from 'redux-pender';

export default combineReducers({
    base,
    auth,
    user,
    calendar,
    pender: penderReducer
});
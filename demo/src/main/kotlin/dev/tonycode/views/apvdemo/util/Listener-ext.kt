package dev.tonycode.views.apvdemo.util


fun consumed(action: () -> Unit): Boolean {
    action.invoke()
    return true
}

<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\OnlineRequestController;

Route::get('/', [OnlineRequestController::class, 'index'])->name('online-requests.index');
Route::post('/request-car', [OnlineRequestController::class, 'store'])->name('online-requests.store');

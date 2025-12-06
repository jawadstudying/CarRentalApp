<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class RequestEntry extends Model
{
    protected $table = 'requests';
    protected $primaryKey = 'request_id';
    public $timestamps = false; // DB handles created_at default

    protected $fillable = [
        'first_name',
        'last_name',
        'phone',
        'email',
        'car_id',
        // created_at is filled by DB default CURRENT_TIMESTAMP
    ];
}


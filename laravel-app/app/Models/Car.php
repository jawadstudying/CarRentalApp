<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Car extends Model
{
    protected $table = 'cars';
    protected $primaryKey = 'car_id';
    public $timestamps = false; // no created_at / updated_at columns

    protected $fillable = [
        'license_plate',
        'brand',
        'model',
        'year',
        'daily_rate',
        'status',
        'mileage',
    ];
}

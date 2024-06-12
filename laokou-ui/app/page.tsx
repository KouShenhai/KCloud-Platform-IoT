import React from 'react';
import { nanoid } from '@reduxjs/toolkit';

import Longin from '@/app/_client/component/login/Login';
import getPublicKey from '@/app/_server/lib/publicKey';
import imgage from '@/app/_server/lib/imgae';
import tenantsId from '@/app/_server/lib/tenantsId';
import tenantsOptionList from '@/app/_server/lib/tenantsOption';

const page: React.FC = async () => {
    const uuid: string = nanoid();
    const result = await Promise.all([getPublicKey(), imgage(uuid), tenantsId(), tenantsOptionList()]);
    const options = [
        {
            value: '0',
            label: '老寇云集团',
            children: [
                {
                    value: 'admin123',
                    label: 'admin',

                },
                {
                    value: 'test123',
                    label: 'test',

                },
                {
                    value: 'test123-',
                    label: 'laok5',

                },
            ],
        }
    ];

    const accounts = (): any[] => {
        const account = result[3];
        if (account) {
            const accounts_new: any[] = [];
            account.forEach((x: any) => {
                if ('阿里集团' === x.label) {
                    accounts_new.push(
                        {
                            value: x.value,
                            label: x.label,
                            children: [{
                                label: 'tenant',
                                value: 'tenant123',
                            }]
                        })
                } else {
                    accounts_new.push({ value: x.value, label: x.label })
                }
            });
            // console.log(accounts_new)
            return [...options, ...accounts_new.reverse()]
        }
        return options;
    }

    return (
       
        <Longin data={{ pk: result[0], image: result[1], tenantsId: result[2], accounts: accounts(), uuid: uuid }} />
       
    );
};

export default page;
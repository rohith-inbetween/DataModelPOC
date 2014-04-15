package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QAttributeInstance is a Querydsl query type for AttributeInstance
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAttributeInstance extends EntityPathBase<AttributeInstance> {

    private static final long serialVersionUID = 1122202916L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttributeInstance attributeInstance = new QAttributeInstance("attributeInstance");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QAttribute attribute;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final QValue value;

    public QAttributeInstance(String variable) {
        this(AttributeInstance.class, forVariable(variable), INITS);
    }

    public QAttributeInstance(Path<? extends AttributeInstance> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttributeInstance(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttributeInstance(PathMetadata<?> metadata, PathInits inits) {
        this(AttributeInstance.class, metadata, inits);
    }

    public QAttributeInstance(Class<? extends AttributeInstance> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attribute = inits.isInitialized("attribute") ? new com.cs.layer3.repository.business.defalt.bo.QAttribute(forProperty("attribute")) : null;
        this.value = inits.isInitialized("value") ? new QValue(forProperty("value")) : null;
    }

}

